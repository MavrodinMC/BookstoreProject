package com.mavro.interfaces;

public interface EmailSender {

    void sendConfirmationLinkToEmail(String to, String email);

    void sendResetPasswordLinkToEmail(String to, String email);
}
