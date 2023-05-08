package com.ugive.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorObject> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ForbiddenChangeException.class)
    public ResponseEntity<ErrorObject> handleForbiddenChangeException(ForbiddenChangeException ex, WebRequest request) {

        ErrorObject errorObject = new ErrorObject();

        errorObject.setStatusCode(HttpStatus.FORBIDDEN.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());

        return new ResponseEntity<>(errorObject, HttpStatus.FORBIDDEN);
    }
}
