package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmailNotFoundException extends RuntimeException {

    public EmailNotFoundException(String message) {
        super(message);
    }
}
