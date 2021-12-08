package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoEmailFoundForAccountException extends RuntimeException {

    public NoEmailFoundForAccountException(String message) {
        super(message);
    }
}
