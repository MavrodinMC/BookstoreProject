package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConfirmationTokenNotFoundException extends RuntimeException {

    public ConfirmationTokenNotFoundException(String message) {
        super(message);
    }
}
