package com.mavro.services;

import com.mavro.dto.ForgotPasswordDto;
import com.mavro.entities.AppUser;
import com.mavro.entities.ForgotPasswordToken;
import com.mavro.exceptions.*;
import com.mavro.interfaces.EmailSender;
import com.mavro.repositories.AppUserRepository;
import com.mavro.repositories.ForgotPasswordTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Service
public class ForgotPasswordService {

    private final ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    public void generateResetPasswordToken(String email) {

        if (email.isEmpty() || email.isBlank()) {
            throw new EmptyInputException();
        }

        AppUser appUser = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(NoEmailFoundForAccountException::new);

        String resetToken = UUID.randomUUID().toString();
        ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
        forgotPasswordToken.setResetToken(resetToken);
        forgotPasswordToken.setRequestedAt(LocalDateTime.now());
        forgotPasswordToken.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        forgotPasswordToken.setAppUser(appUser);

        forgotPasswordTokenRepository.save(forgotPasswordToken);

        String link = "http://localhost:4200/forgot-password-reset?resetToken=" + resetToken;
        emailSender.sendResetPasswordLinkToEmail(appUser.getEmail(), buildEmail(appUser.getUsername(), link));

    }

    public ForgotPasswordToken captureResetToken(String resetToken) {
        
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByResetToken(resetToken)
                .orElseThrow(ResetPasswordTokenNotFoundException::new);

        if (forgotPasswordToken.getUsedAt() != null) {
            throw new ResetPasswordRequestAlreadyUsedException();
        }

        return forgotPasswordToken;
    }

    public void confirmNewPasswordReset(String resetToken, ForgotPasswordDto forgotPasswordDto) {

        if (!checkForInvalidOrEmptyInput(forgotPasswordDto)) {
            throw new EmptyInputException();
        }

        Optional<ForgotPasswordToken> forgotPasswordToken = forgotPasswordTokenRepository.findByResetToken(resetToken);
        forgotPasswordToken.orElseThrow(ResetPasswordTokenNotFoundException::new);
        fetchUser(forgotPasswordToken.get(), forgotPasswordDto);

    }


    @Transactional
    private void fetchUser(ForgotPasswordToken forgotPasswordToken, ForgotPasswordDto forgotPasswordDto) {

        String email = forgotPasswordToken.getAppUser().getEmail();
        AppUser user = appUserRepository.findAppUserByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        if (forgotPasswordToken.getUsedAt() != null) {
            throw new ResetPasswordRequestAlreadyUsedException();
        }

        if (forgotPasswordToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            generateResetPasswordToken(user.getEmail());
            throw new ResetPasswordRequestExpiredException();
        }

        if (!forgotPasswordDto.getNewPassword().equals(forgotPasswordDto.getConfirmNewPassword())) {
            throw new PasswordsMustMatchException();
        }

        forgotPasswordToken.setUsedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(forgotPasswordDto.getNewPassword()));
        appUserRepository.save(user);
    }

    public boolean checkForInvalidOrEmptyInput(ForgotPasswordDto forgotPasswordDto) {

        return !forgotPasswordDto.getNewPassword().isEmpty() && !forgotPasswordDto.getNewPassword().isBlank() && !forgotPasswordDto.getConfirmNewPassword().isEmpty() && !forgotPasswordDto.getConfirmNewPassword().isBlank();
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
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Reset password</span>\n" +
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> You requested to change your password. If this was not you, please, ignore this email. If it was you, please follow the link below to reset your password. </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Reset Password</a> </p></blockquote>\n Link will expire in 30 minutes. <p>See you soon</p>" +
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
