package com.example.awesome_pizza.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class GenericNotFoundException extends AbstractErrorException {

    @Serial
    private static final long serialVersionUID = 4712367407829784673L;

    private static final HttpStatus defaultHttpStatus = HttpStatus.NOT_FOUND;
    private static final CommonErrorCode defaultErrorCode = CommonErrorCode.ERR_404;

    public GenericNotFoundException() {
        super(defaultErrorCode.getDescription(), defaultHttpStatus, defaultErrorCode);
    }

    public GenericNotFoundException(String message) {
        super(message, defaultHttpStatus, defaultErrorCode);
    }
}
