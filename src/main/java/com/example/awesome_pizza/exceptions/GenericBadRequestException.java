package com.example.awesome_pizza.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class GenericBadRequestException extends AbstractErrorException {

    @Serial
    private static final long serialVersionUID = -873368474198465663L;

    private static final HttpStatus defaultHttpStatus = HttpStatus.BAD_REQUEST;
    private static final CommonErrorCode defaultErrorCode = CommonErrorCode.ERR_400;

    public GenericBadRequestException() {
        super(defaultErrorCode.getDescription(), defaultHttpStatus, defaultErrorCode);
    }

    public GenericBadRequestException(String message) {
        super(message, defaultHttpStatus, defaultErrorCode);
    }
}
