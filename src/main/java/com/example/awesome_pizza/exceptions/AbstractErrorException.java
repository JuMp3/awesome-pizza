package com.example.awesome_pizza.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class AbstractErrorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -2754333785731923915L;

    private final transient IErrorCode errorCode;
    private final HttpStatusCode httpStatus;

    public AbstractErrorException(String message, HttpStatusCode httpStatus, IErrorCode errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public AbstractErrorException(String message, HttpStatusCode httpStatus, IErrorCode errorCode, Throwable t) {
        super(message, t);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}
