package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RefreshTokenNotFoundException extends RuntimeException {

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }
}
