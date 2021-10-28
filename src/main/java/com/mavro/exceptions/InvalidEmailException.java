package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidEmailException extends RuntimeException {

    public InvalidEmailException(String message) {
        super(message);
    }
}
