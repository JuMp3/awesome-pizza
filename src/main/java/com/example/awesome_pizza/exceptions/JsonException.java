package com.example.awesome_pizza.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class JsonException extends AbstractErrorException {

    private static final HttpStatus defaultHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final CommonErrorCode defaultErrorCode = CommonErrorCode.ERR_JSON;

    @Serial
    private static final long serialVersionUID = -4087953360836028061L;

    public JsonException() {
        super(defaultErrorCode.getDescription(), defaultHttpStatus, defaultErrorCode);
    }

    public JsonException(Exception e) {
        super(e.getClass().getName() + ": " + e.getMessage(), defaultHttpStatus, defaultErrorCode);
    }

    public JsonException(String message) {
        super(message, defaultHttpStatus, defaultErrorCode);
    }
}
