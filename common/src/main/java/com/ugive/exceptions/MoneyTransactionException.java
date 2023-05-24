package com.ugive.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MoneyTransactionException extends RuntimeException {
    public MoneyTransactionException(String message) {
        super(message);
    }
}
