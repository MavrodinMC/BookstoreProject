package com.mavro.controllers;

import com.mavro.dto.UserPersonalDetailsDto;
import com.mavro.entities.UserPersonalDetails;
import com.mavro.services.UserPersonalDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/bookstore")
public class UserPersonalDetailsController {

    private final UserPersonalDetailsService userPersonalDetailsService;

    @GetMapping("/user/details/{email}")
    public ResponseEntity<UserPersonalDetails> getUserPersonalDetails(@PathVariable String email) {

        return new ResponseEntity<>(userPersonalDetailsService.getUserPersonalDetails(email), HttpStatus.OK);
    }

    @PutMapping("/user/update/details/{id}")
    public ResponseEntity<UserPersonalDetails> updateUserPersonalDetails(@PathVariable int id, @RequestBody UserPersonalDetailsDto userPersonalDetailsDto) {

        userPersonalDetailsService.updateUserPersonalDetails(id, userPersonalDetailsDto);

        return new ResponseEntity<>( HttpStatus.OK);
    }
}
