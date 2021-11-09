package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException(String message) {
        super(message);
    }
}
