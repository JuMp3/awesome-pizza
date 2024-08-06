package com.example.awesome_pizza.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class GenericBadGatewayException extends AbstractErrorException {

    @Serial
    private static final long serialVersionUID = 2835320989879754406L;

    private static final HttpStatus defaultHttpStatus = HttpStatus.BAD_GATEWAY;
    private static final CommonErrorCode defaultErrorCode = CommonErrorCode.ERR_502;

    public GenericBadGatewayException() {
        super(defaultErrorCode.getDescription(), defaultHttpStatus, defaultErrorCode);
    }

    public GenericBadGatewayException(String message) {
        super(message, defaultHttpStatus, defaultErrorCode);
    }
}
