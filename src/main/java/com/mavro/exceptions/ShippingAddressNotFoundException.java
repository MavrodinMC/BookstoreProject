package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ShippingAddressNotFoundException extends RuntimeException {


    public ShippingAddressNotFoundException(String message) {
        super(message);
    }

}
