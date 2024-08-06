package com.example.awesome_pizza.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class GenericServiceUnavailableException extends AbstractErrorException {

    @Serial
    private static final long serialVersionUID = -7467507983979608959L;

    private static final HttpStatus defaultHttpStatus = HttpStatus.SERVICE_UNAVAILABLE;
    private static final CommonErrorCode defaultErrorCode = CommonErrorCode.ERR_503;

    public GenericServiceUnavailableException() {
        super(defaultErrorCode.getDescription(), defaultHttpStatus, defaultErrorCode);
    }

    public GenericServiceUnavailableException(String message) {
        super(message, defaultHttpStatus, defaultErrorCode);
    }
}
