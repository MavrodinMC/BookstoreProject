package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FailedToSendEmailException extends RuntimeException {

    public FailedToSendEmailException(String message) {
        super(message);
    }
}
