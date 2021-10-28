package com.mavro.controllers;

import com.mavro.entities.AppUser;
import com.mavro.services.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/backend/bookstore/users")
public class AppUserController {

    private final AppUserService appUserService;

    @GetMapping("/all")
    public ResponseEntity<List<AppUser>> getAllUsers() {

        return new ResponseEntity<>(appUserService.getAllUsers(), HttpStatus.OK);
    }
}
