package com.example.awesome_pizza.exceptions;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class GenericGatewayTimeoutException extends AbstractErrorException {

    @Serial
    private static final long serialVersionUID = -7388868491756895593L;

    private static final HttpStatus defaultHttpStatus = HttpStatus.GATEWAY_TIMEOUT;
    private static final CommonErrorCode defaultErrorCode = CommonErrorCode.ERR_504;

    public GenericGatewayTimeoutException() {
        super(defaultErrorCode.getDescription(), defaultHttpStatus, defaultErrorCode);
    }

    public GenericGatewayTimeoutException(String message) {
        super(message, defaultHttpStatus, defaultErrorCode);
    }
}
