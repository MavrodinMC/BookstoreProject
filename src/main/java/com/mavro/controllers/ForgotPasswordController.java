package com.mavro.controllers;

import com.mavro.dto.ForgotPasswordDto;
import com.mavro.entities.ForgotPasswordToken;
import com.mavro.services.ForgotPasswordService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/bookstore")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;


    @PostMapping("/generateReset")
    public ResponseEntity<String> sendResetMail(@RequestBody String email) {

        forgotPasswordService.generateResetPasswordToken(email);
        return new ResponseEntity<>("A password reset was issued on your mail, check your email to change your password.", HttpStatus.OK);
    }

    @GetMapping("/forgot")
    public ResponseEntity<ForgotPasswordToken> captureResetToken(@RequestParam(name = "resetToken") String resetToken) {

        return new ResponseEntity<>(forgotPasswordService.captureResetToken(resetToken), HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam(name = "resetToken") String resetToken, @RequestBody ForgotPasswordDto forgotPasswordDto) {

        forgotPasswordService.confirmNewPasswordReset(resetToken, forgotPasswordDto);
        return new ResponseEntity<>("You have successfully changed your password", HttpStatus.OK);
    }
}
