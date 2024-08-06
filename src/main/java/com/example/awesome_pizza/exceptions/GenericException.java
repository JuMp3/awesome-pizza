package com.example.awesome_pizza.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;

public class GenericException extends AbstractErrorException {

    @Serial
    private static final long serialVersionUID = -7110556271756647368L;

    private static final HttpStatus defaultHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final CommonErrorCode defaultErrorCode = CommonErrorCode.ERR_500;

    public GenericException() {
        super(defaultErrorCode.getDescription(), defaultHttpStatus, defaultErrorCode);
    }

    public GenericException(String message) {
        super(message, defaultHttpStatus, defaultErrorCode);
    }

    public GenericException(HttpStatusCode httpStatus) {
        super(defaultErrorCode.getDescription(), httpStatus, defaultErrorCode);
    }
}
