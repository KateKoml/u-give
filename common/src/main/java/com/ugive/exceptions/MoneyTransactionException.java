package com.ugive.exceptions;

import java.io.Serial;

public class MoneyTransactionException extends RuntimeException {
    public MoneyTransactionException(String message) {
        super(message);
    }
}
