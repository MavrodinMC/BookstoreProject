package com.mavro.services;

import com.mavro.dto.RegistrationRequest;
import com.mavro.entities.AppUser;
import com.mavro.entities.ConfirmationToken;
import com.mavro.exceptions.*;
import com.mavro.repositories.AppUserRepository;
import com.mavro.repositories.ConfirmationTokenRepository;
import com.mavro.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AuthService {

         private final EmailValidatorService emailValidatorService;
         private final AppUserRepository appUserRepository;
         private final RoleRepository roleRepository;
         private final ConfirmationTokenRepository confirmationTokenRepository;
         private final PasswordEncoder passwordEncoder;


    public void registerUser(RegistrationRequest registrationRequest) {

        if (!checkForInvalidOrEmptyInput(registrationRequest)) {

            throw new EmptyInputException();
        }

        boolean isEmailValid = emailValidatorService.test(registrationRequest.getEmail());

        if (!isEmailValid) {
            throw new InvalidEmailException();
        }

        AppUser user = new AppUser();

        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRoles(Set.of(roleRepository.findRoleByName("USER")));
        user.setCreatedAt(LocalDateTime.now());

        appUserRepository.save(user);

        generateConfirmationToken(user);
    }

    public String generateConfirmationToken(AppUser appUser) {

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setAppUser(appUser);

        confirmationTokenRepository.save(confirmationToken);

        return token;
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

        if (confirmationToken.getConfirmedAt() != null) {
            throw new ConfirmationTokenAlreadyConfirmedException();
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            generateConfirmationToken(user);
            throw new ConfirmationTokenHasExpiredException();
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        user.setLocked(false);
        user.setEnabled(true);
        appUserRepository.save(user);
    }

    public boolean checkForInvalidOrEmptyInput(RegistrationRequest registrationRequest) {

        return !registrationRequest.getEmail().isEmpty() && !registrationRequest.getPassword().isEmpty()
              && !registrationRequest.getEmail().isBlank() && !registrationRequest.getPassword().isBlank();
    }
}
