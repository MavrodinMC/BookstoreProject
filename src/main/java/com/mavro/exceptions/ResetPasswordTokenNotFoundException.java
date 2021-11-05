package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResetPasswordTokenNotFoundException extends RuntimeException {

    public ResetPasswordTokenNotFoundException(String message) {
        super(message);
    }
}
