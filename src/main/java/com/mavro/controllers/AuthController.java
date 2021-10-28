package com.mavro.controllers;

import com.mavro.dto.RegistrationRequest;
import com.mavro.entities.AppUser;
import com.mavro.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/bookstore")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AppUser> registerUser(@RequestBody RegistrationRequest registrationRequest) {

        authService.registerUser(registrationRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
