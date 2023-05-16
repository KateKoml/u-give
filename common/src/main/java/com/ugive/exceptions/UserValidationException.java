package com.ugive.exceptions;

import java.io.Serial;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}
