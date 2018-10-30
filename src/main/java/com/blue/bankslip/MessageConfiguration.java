package com.blue.bankslip;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MessageConfiguration {
    @ResponseStatus(value= HttpStatus.CREATED, reason="No such Order")
    public class OrderNotFoundException extends RuntimeException {
        // ...
    }
}
