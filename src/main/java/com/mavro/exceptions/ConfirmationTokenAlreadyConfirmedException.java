package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConfirmationTokenAlreadyConfirmedException extends RuntimeException {

    public ConfirmationTokenAlreadyConfirmedException(String message) {
        super(message);
    }
}
