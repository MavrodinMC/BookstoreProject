package com.mavro.services.impl;

import com.mavro.config.EmailConfig;
import com.mavro.exceptions.FailedToSendEmailException;
import com.mavro.interfaces.EmailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@AllArgsConstructor
@Slf4j
@Service
public class EmailSenderImpl implements EmailSender {

    private final EmailConfig emailConfig;

    @Override
    @Async
    public void sendConfirmationLinkToEmail(String to, String email) {

         try {

             JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
             mailSender.setHost(emailConfig.getHost());
             mailSender.setPort(emailConfig.getPort());
             mailSender.setUsername(emailConfig.getUsername());
             mailSender.setPassword(emailConfig.getPassword());

             MimeMessage mimeMessage = mailSender.createMimeMessage();
             MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

             helper.setText(email, true);
             helper.setTo(to);
             helper.setSubject("Confirm your account");
             helper.setFrom("noreply@bookstore.ro");
             mailSender.send(mimeMessage);

         } catch (MessagingException e) {
             log.error("Failed to send email");
             throw new FailedToSendEmailException(to);
         }
    }

    @Override
    public void sendResetPasswordLinkToEmail(String to, String email) {

        try {

            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(emailConfig.getHost());
            mailSender.setPort(emailConfig.getPort());
            mailSender.setUsername(emailConfig.getUsername());
            mailSender.setPassword(emailConfig.getPassword());

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Password reset");
            helper.setFrom("noreply@bookstore.ro");
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            log.error("Failed to send email");
            throw new FailedToSendEmailException(to);
        }
    }

}
