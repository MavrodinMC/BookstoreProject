package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class KeyException extends RuntimeException {

    public KeyException(String message) {
        super(message);
    }
}
