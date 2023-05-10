package com.ugive.exceptions;

import java.io.Serial;

public class UserValidationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 3;

    public UserValidationException(String message) {
        super(message);
    }
}
