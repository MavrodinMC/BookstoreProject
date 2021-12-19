package com.mavro.services;

import com.mavro.dto.AuthenticationResponse;
import com.mavro.dto.LoginRequest;
import com.mavro.dto.RefreshTokenRequest;
import com.mavro.dto.RegistrationRequest;
import com.mavro.entities.AppUser;
import com.mavro.entities.ConfirmationToken;
import com.mavro.entities.UserPersonalDetails;
import com.mavro.exceptions.*;
import com.mavro.interfaces.EmailSender;
import com.mavro.jwt.JwtProvider;
import com.mavro.repositories.AppUserRepository;
import com.mavro.repositories.ConfirmationTokenRepository;
import com.mavro.repositories.RoleRepository;
import com.mavro.repositories.UserPersonalDetailsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Service
public class AuthService {

    private final EmailValidatorService emailValidatorService;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final UserPersonalDetailsRepository userPersonalDetailsRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;


    public void registerUser(RegistrationRequest registrationRequest) {

        if (!checkForInvalidOrEmptyInputOnSignIn(registrationRequest)) {
            throw new EmptyInputException();
        }

        boolean isEmailValid = emailValidatorService.test(registrationRequest.getEmail());

        if (!isEmailValid) {
            throw new InvalidEmailException();
        }

        AppUser user = new AppUser();

        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRoles(Set.of(roleRepository.findRoleByName("USER")));
        user.setCreatedAt(LocalDateTime.now());

        UserPersonalDetails userPersonalDetails = new UserPersonalDetails();

        appUserRepository.save(user);

        userPersonalDetails.setAppUser(user);
        userPersonalDetailsRepository.save(userPersonalDetails);

        generateConfirmationToken(user);

    }

    public AuthenticationResponse login(LoginRequest loginRequest) {

        if (!checkForInvalidOrEmptyInputOnLogin(loginRequest)) {
            throw new EmptyInputException();
        }

        try {

            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authenticate);
            String token = jwtProvider.generateToken(authenticate);

            return AuthenticationResponse.builder()
                    .authenticationToken(token)
                    .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                    .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                    .email(loginRequest.getEmail())
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException();
        }

    }

    public void generateConfirmationToken(AppUser appUser) {

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setAppUser(appUser);

        confirmationTokenRepository.save(confirmationToken);

        String link = "http://localhost:4200/accountConfirmation?token=" + token;
        emailSender.sendConfirmationLinkToEmail(appUser.getEmail(), buildEmail(appUser.getUsername(), link));

    }

    public void confirmAccount(String token) {

        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(token);
        confirmationToken.orElseThrow(ConfirmationTokenNotFoundException::new);
        fetchUserAndEnable(confirmationToken.get());

    }

    @Transactional
    private void fetchUserAndEnable(ConfirmationToken confirmationToken) {

        String email = confirmationToken.getAppUser().getEmail();
        AppUser user = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        if (confirmationToken.getConfirmedAt() != null || confirmationToken.getAppUser().getEnabled()) {
            throw new ConfirmationTokenAlreadyConfirmedException();
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            generateConfirmationToken(user);
            confirmationTokenRepository.deleteById(confirmationToken.getId());
            throw new ConfirmationTokenHasExpiredException();
        }


        confirmationToken.setConfirmedAt(LocalDateTime.now());
        user.setLocked(false);
        user.setEnabled(true);
        appUserRepository.save(user);
    }

    public boolean checkForInvalidOrEmptyInputOnSignIn(RegistrationRequest registrationRequest) {

        return !registrationRequest.getUsername().isEmpty() && !registrationRequest.getUsername().isBlank() && !registrationRequest.getEmail().isEmpty() && !registrationRequest.getPassword().isEmpty()
                && !registrationRequest.getEmail().isBlank() && !registrationRequest.getPassword().isBlank();
    }

    public boolean checkForInvalidOrEmptyInputOnLogin(LoginRequest loginRequest) {

        return !loginRequest.getEmail().isEmpty() && !loginRequest.getEmail().isBlank() && !loginRequest.getPassword().isEmpty() && !loginRequest.getPassword().isBlank();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getEmail());

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
                .email(refreshTokenRequest.getEmail())
                .build();
    }

    @Transactional
    public AppUser getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        return appUserRepository.findAppUserByEmail(principal.getUsername())
                .orElseThrow(EmailNotFoundException::new);
    }

    public boolean isLoggedIn() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }


    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }


}
