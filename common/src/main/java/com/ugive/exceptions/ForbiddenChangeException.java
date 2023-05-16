package com.ugive.exceptions;

import java.io.Serial;

public class ForbiddenChangeException extends RuntimeException {
    public ForbiddenChangeException(String message) {
        super(message);
    }
}
