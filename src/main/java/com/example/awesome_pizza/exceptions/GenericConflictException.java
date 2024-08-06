package com.example.awesome_pizza.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class GenericConflictException extends AbstractErrorException {

    @Serial
    private static final long serialVersionUID = 3814548471349316584L;

    private static final HttpStatus defaultHttpStatus = HttpStatus.CONFLICT;
    private static final CommonErrorCode defaultErrorCode = CommonErrorCode.ERR_409;

    public GenericConflictException() {
        super(defaultErrorCode.getDescription(), defaultHttpStatus, defaultErrorCode);
    }

    public GenericConflictException(String message) {
        super(message, defaultHttpStatus, defaultErrorCode);
    }
}
