package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PasswordsMustMatchException extends RuntimeException {

    public PasswordsMustMatchException(String message) {
        super(message);
    }
}
