package com.mavro.exceptions;

import com.mavro.dto.AuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmptyInputException.class)
    public ResponseEntity<String> handleEmptyInputException(EmptyInputException emptyInputException) {

        return new ResponseEntity<>("Malformed or empty fields", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<String> handleEmailNotFoundException(EmailNotFoundException emailNotFoundException) {

        return new ResponseEntity<>("The user was not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<String> handleInvalidEmail(InvalidEmailException invalidEmailException) {

        return new ResponseEntity<>("The mail is invalid, check the format and try again", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConfirmationTokenNotFoundException.class)
    public ResponseEntity<String> handleConfirmationTokenNotFoundException(ConfirmationTokenNotFoundException confirmationTokenNotFoundException) {

        return new ResponseEntity<>("The requested confirmation token was not found!", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConfirmationTokenHasExpiredException.class)
    public ResponseEntity<String> handleExpiredConfirmationToken(ConfirmationTokenHasExpiredException confirmationTokenHasExpiredException) {

        return new ResponseEntity<>("The confirmation token has expired. A new token was issued to your email address, please click the link to confirm your account.", HttpStatus.GONE);
    }

    @ExceptionHandler(ConfirmationTokenAlreadyConfirmedException.class)
    public ResponseEntity<String> handleConfirmationTokenAlreadyConfirmed(ConfirmationTokenAlreadyConfirmedException confirmationTokenAlreadyConfirmedException) {

        return new ResponseEntity<>("The confirmation token was already used.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FailedToSendEmailException.class)
    public ResponseEntity<String> handleFailedToSendEmail(FailedToSendEmailException failedToSendEmailException) {

        return new ResponseEntity<>("Failed to send email to " , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResetPasswordRequestExpiredException.class)
    public ResponseEntity<String> handleResetPasswordRequestExpired(ResetPasswordRequestExpiredException resetPasswordRequestExpiredException) {

        return new ResponseEntity<>("The request expired", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResetPasswordRequestAlreadyUsedException.class)
    public ResponseEntity<String> handleResetPasswordRequestAlreadyUsed(ResetPasswordRequestAlreadyUsedException resetPasswordRequestAlreadyUsedException) {

        return new ResponseEntity<>("This request was already completed", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResetPasswordTokenNotFoundException.class)
    public ResponseEntity<String> handleResetPasswordTokenNotFound(ResetPasswordTokenNotFoundException resetPasswordTokenNotFoundException) {

        return new ResponseEntity<>("The reset token was not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PasswordsMustMatchException.class)
    public ResponseEntity<String> handlePasswordsMustMatch(PasswordsMustMatchException passwordsMustMatchException) {

        return new ResponseEntity<>("Passwords must match", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(KeyException.class)
    public ResponseEntity<String> handleKeyException(KeyException keyException) {

        return new ResponseEntity<>("Problem while loading keystore...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AuthenticationResponse> handleBadCredentials(BadCredentialsException badCredentialsException) {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setAuthenticationToken(null);
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);
    }

}
