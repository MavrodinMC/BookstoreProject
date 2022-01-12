package com.mavro.controllers;

import com.mavro.dto.ShippingAddressDto;
import com.mavro.entities.ShippingAddress;
import com.mavro.services.ShippingAddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/bookstore")
public class ShippingAddressController {

    private final ShippingAddressService shippingAddressService;

    @GetMapping("/shippingAddress/{email}")
    public ResponseEntity<List<ShippingAddress>> getAllAddressesForAUser(@PathVariable(name = "email") String email) {

        return new ResponseEntity<>(shippingAddressService.getAllAddressesForAUser(email), HttpStatus.OK);
    }

    @PostMapping("/save/shippingAddress/{email}")
    public ResponseEntity<?> saveAnAddressForAUser(@PathVariable(name = "email") String email, @RequestBody ShippingAddressDto shippingAddressDto) {

        shippingAddressService.saveAddressForAUser(email, shippingAddressDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/shippingAddress/update")
    public ResponseEntity<?> updateAnAddressForAUser(@RequestBody ShippingAddress shippingAddress) {

        shippingAddressService.updateAnAddressForAUser(shippingAddress);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/shippingAddress/delete/{shippingAddressId}/{email}")
    public ResponseEntity<?> deleteAnAddressForAUser(@PathVariable(name = "shippingAddressId") int shippingAddressId, @PathVariable(name = "email") String email) {

        shippingAddressService.deleteAnAddressForAUser(shippingAddressId, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
