package com.mavro.controllers;

import com.mavro.dto.RegistrationRequest;
import com.mavro.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/bookstore")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) {

        authService.registerUser(registrationRequest);

        return new ResponseEntity<>("You have successfully registered to our bookstore! Please check your email for the activation link in order to have full access to all of our site and your account features! Thank you!", HttpStatus.CREATED);
    }

    @GetMapping("/accountConfirmation")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        authService.confirmAccount(token);
        return new ResponseEntity<>("Account activated successfully.", HttpStatus.OK);
    }

    // f494f2e8-ecb9-40f6-9176-7ab141bb59f3

}
