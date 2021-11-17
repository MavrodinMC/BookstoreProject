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
@RequestMapping("/bookstore")
public class UserPersonalDetailsController {

    private final UserPersonalDetailsService userPersonalDetailsService;

    @GetMapping("/user/details/{id}")
    public ResponseEntity<UserPersonalDetails> getUserPersonalDetails(@PathVariable int id) {

        return new ResponseEntity<>(userPersonalDetailsService.getUserPersonalDetails(id), HttpStatus.OK);
    }

    @PostMapping("/user/details")
    public ResponseEntity<UserPersonalDetails> saveUserPersonalDetails(@RequestParam(name = "email") String email, @RequestBody UserPersonalDetailsDto userPersonalDetailsDto) {

        return new ResponseEntity<>(userPersonalDetailsService.saveUserPersonalDetails(email, userPersonalDetailsDto), HttpStatus.CREATED);
    }

    @PutMapping("/user/update/details/{id}")
    public ResponseEntity<UserPersonalDetails> updateUserPersonalDetails(@PathVariable int id, @RequestBody UserPersonalDetailsDto userPersonalDetailsDto) {

        userPersonalDetailsService.updateUserPersonalDetails(id, userPersonalDetailsDto);

        return new ResponseEntity<>( HttpStatus.OK);
    }
}
