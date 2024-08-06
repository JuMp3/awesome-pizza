package com.example.awesome_pizza.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class GenericForbiddenException extends AbstractErrorException {

    @Serial
    private static final long serialVersionUID = 3390914537974953473L;

    private static final HttpStatus defaultHttpStatus = HttpStatus.FORBIDDEN;
    private static final CommonErrorCode defaultErrorCode = CommonErrorCode.ERR_403;

    public GenericForbiddenException() {
        super(defaultErrorCode.getDescription(), defaultHttpStatus, defaultErrorCode);
    }

    public GenericForbiddenException(String message) {
        super(message, defaultHttpStatus, defaultErrorCode);
    }
}
