package com.ugive.exceptions;

import java.io.Serial;

public class ForbiddenChangeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2;

    public ForbiddenChangeException(String message) {
        super(message);
    }
}
