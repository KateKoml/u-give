package com.ugive.exceptions;

import java.io.Serial;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
