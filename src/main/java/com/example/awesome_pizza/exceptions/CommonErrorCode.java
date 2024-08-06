package com.example.awesome_pizza.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode implements IErrorCode {

    ERR_400(HttpStatus.BAD_REQUEST.getReasonPhrase()),
    ERR_401(HttpStatus.UNAUTHORIZED.getReasonPhrase()),
    ERR_404(HttpStatus.NOT_FOUND.getReasonPhrase()),
    ERR_403(HttpStatus.FORBIDDEN.getReasonPhrase()),
    ERR_409(HttpStatus.CONFLICT.getReasonPhrase()),
    ERR_500(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),
    ERR_JSON("Exception during processing json"),
    ERR_502(HttpStatus.BAD_GATEWAY.getReasonPhrase()),
    ERR_503(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase()),
    ERR_504(HttpStatus.GATEWAY_TIMEOUT.getReasonPhrase());

    private final String description;

    CommonErrorCode(String description) {
        this.description = description;
    }
}
