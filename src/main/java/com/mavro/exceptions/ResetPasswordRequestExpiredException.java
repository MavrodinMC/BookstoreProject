package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResetPasswordRequestExpiredException extends RuntimeException {

    ResetPasswordRequestExpiredException(String message) {
        super(message);
    }
}
