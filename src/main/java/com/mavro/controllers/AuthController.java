package com.mavro.controllers;

import com.mavro.dto.AuthenticationResponse;
import com.mavro.dto.LoginRequest;
import com.mavro.dto.RefreshTokenRequest;
import com.mavro.dto.RegistrationRequest;
import com.mavro.services.AuthService;
import com.mavro.services.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/bookstore")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {

        authService.registerUser(registrationRequest);

        return new ResponseEntity<>("You have successfully registered to our bookstore! Please check your email for the activation link in order to have full access to all of our site and your account features! Thank you!", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {

        return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String token) {

        refreshTokenService.deleteRefreshToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        return authService.refreshToken(refreshTokenRequest);
    }

    @GetMapping("/accountConfirmation")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        authService.confirmAccount(token);
        return new ResponseEntity<>("Account activated successfully.", HttpStatus.OK);
    }



}
