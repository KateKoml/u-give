package com.ugive.exceptions;

import java.io.Serial;

public class ModifyingChatException extends RuntimeException{
    public ModifyingChatException(String message) {
        super(message);
    }
}
