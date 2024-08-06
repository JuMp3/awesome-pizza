package com.example.awesome_pizza.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class GenericUnauthorizedException extends AbstractErrorException {

    @Serial
    private static final long serialVersionUID = 6213657130253989120L;

    private static final HttpStatus defaultHttpStatus = HttpStatus.UNAUTHORIZED;
    private static final CommonErrorCode defaultErrorCode = CommonErrorCode.ERR_401;

    public GenericUnauthorizedException() {
        super(defaultErrorCode.getDescription(), defaultHttpStatus, defaultErrorCode);
    }

    public GenericUnauthorizedException(String message) {
        super(message, defaultHttpStatus, defaultErrorCode);
    }
}
