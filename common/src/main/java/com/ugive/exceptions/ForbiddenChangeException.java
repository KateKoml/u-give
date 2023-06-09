package com.ugive.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenChangeException extends RuntimeException {
    public ForbiddenChangeException(String message) {
        super(message);
    }
}
