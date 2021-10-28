package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConfirmationTokenHasExpiredException extends RuntimeException {

    public ConfirmationTokenHasExpiredException(String message) {
        super(message);
    }
}
