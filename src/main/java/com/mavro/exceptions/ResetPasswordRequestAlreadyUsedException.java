package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ResetPasswordRequestAlreadyUsedException extends RuntimeException {

    public ResetPasswordRequestAlreadyUsedException(String message) {
        super(message);
    }
}
