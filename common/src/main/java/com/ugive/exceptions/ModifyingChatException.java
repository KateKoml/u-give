package com.ugive.exceptions;

import java.io.Serial;

public class ModifyingChatException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 5;

    public ModifyingChatException(String message) {
        super(message);
    }
}
