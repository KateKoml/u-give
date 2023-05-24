package com.ugive.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ModifyingChatException extends RuntimeException {
    public ModifyingChatException(String message) {
        super(message);
    }
}
