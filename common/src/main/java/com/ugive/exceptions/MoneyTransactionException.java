package com.ugive.exceptions;

import java.io.Serial;

public class MoneyTransactionException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4;

    public MoneyTransactionException(String message) {
        super(message);
    }
}
