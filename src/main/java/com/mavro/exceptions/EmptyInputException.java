package com.mavro.exceptions;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmptyInputException extends RuntimeException{

    public EmptyInputException(String message) {
        super(message);
    }
}
