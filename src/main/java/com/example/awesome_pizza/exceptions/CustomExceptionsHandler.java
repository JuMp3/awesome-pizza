package com.example.awesome_pizza.exceptions;

import com.example.awesome_pizza.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.DateTimeException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionException;
import java.util.stream.Stream;

@Log4j2
@RestControllerAdvice
public class CustomExceptionsHandler extends ResponseEntityExceptionHandler implements ICustomExceptionsHandler {

    @Value("${spring.profiles.active}")
    protected String profile;

    @ExceptionHandler({AbstractErrorException.class})
    public ResponseEntity<ErrorResponse> abstractErrorExceptionExceptionHandler(AbstractErrorException ex,
                                                                                ServletWebRequest request) {

        String msg = ex.getErrorCode().getDescription() + ": " + ex.getMessage();
        if (ex.getHttpStatus().is5xxServerError()) {
            log.error(msg);
        } else {
            log.warn(msg);
        }

        return new ResponseEntity<>(
                ICustomExceptionsHandler.buildErrorResponse(
                        profile,
                        request.getRequest(),
                        ex.getErrorCode().name(),
                        ex.getErrorCode().getDescription(),
                        ex.getMessage()
                ),
                ex.getHttpStatus());
    }

    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<ErrorResponse> handleCompletionException(Exception e, WebRequest request) {
        if (AbstractErrorException.class.isAssignableFrom(e.getCause().getClass())) {
            return abstractErrorExceptionExceptionHandler(
                    ((AbstractErrorException) e.getCause()),
                    (ServletWebRequest) request);
        } else {
            return handleGenericException(e, ((ServletWebRequest) request).getRequest());
        }
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manve,
                                                               @Nullable HttpHeaders headers,
                                                               HttpStatusCode status,
                                                               WebRequest request) {

        return ResponseEntity.badRequest().body(
                manve.getBindingResult().getFieldErrors()
                        .stream()
                        .filter(fieldError -> !StringUtils.isEmpty(fieldError.getDefaultMessage()))
                        .distinct()
                        .map(fieldError -> {

                            StringBuilder message = new StringBuilder();

                            String msg = "";
                            try {
                                msg = JsonUtils.stringify(fieldError.getRejectedValue());
                            } catch (JsonProcessingException ignored) {

                            }

                            log.warn(
                                    message
                                            .append("Error '")
                                            .append(fieldError.getDefaultMessage())
                                            .append("' on field ")
                                            .append(fieldError.getField())
                                            .append(" -> rejected value is '")
                                            .append(msg)
                                            .append("'")
                                            .toString());

                            return ICustomExceptionsHandler
                                    .buildErrorResponse(
                                            profile,
                                            ((ServletWebRequest) request).getRequest(),
                                            String.join("_",
                                                    fieldError.getField().substring(fieldError.getField().lastIndexOf(".") + 1),
                                                    fieldError.getCode()),
                                            "Validation error on field " + fieldError.getField(),
                                            message);
                        }).toList()
        );
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<ErrorResponse> handleHttpStatusCodeException(HttpStatusCodeException hsce,
                                                                       HttpServletRequest request) {
        return new ResponseEntity<>(
                ICustomExceptionsHandler.buildErrorResponse(
                        profile,
                        request,
                        "HTTPERRCONN",
                        "Http connection error",
                        Stream
                                .of(hsce.getMessage(), hsce.getResponseBodyAsString())
                                .toList()),
                hsce.getStatusCode());
    }

    @ExceptionHandler({DataAccessException.class, HibernateException.class})
    public ResponseEntity<ErrorResponse> handleMongoException(Exception e, HttpServletRequest request) {
        return ResponseEntity.internalServerError().body(
                ICustomExceptionsHandler.buildErrorResponse(
                        profile,
                        request,
                        "DBERR",
                        "Database error",
                        e.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e, HttpServletRequest request) {

        if (e instanceof BindException be) {
            ErrorResponse response = getBindExceptionResponse(be.getBindingResult(), request);
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.internalServerError().body(
                ICustomExceptionsHandler.buildErrorResponse(
                        profile,
                        request,
                        "INTSERVERERR",
                        "Internal server error",
                        e.getMessage())
        );
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException iae, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(ICustomExceptionsHandler.buildErrorResponse(this.profile,
                request,
                "ERRVALIDATION002",
                "Input validation error",
                iae.getMessage())
        );
    }

    @ExceptionHandler({DateTimeException.class})
    public ResponseEntity<ErrorResponse> handleDateTimeException(DateTimeException dte, HttpServletRequest request) {
        return ResponseEntity.badRequest().body(ICustomExceptionsHandler.buildErrorResponse(this.profile,
                request,
                "ERRVALIDATIONDATE",
                "Date conversion error",
                dte.getMessage())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException manve,
                                                                     WebRequest request) {
        return ResponseEntity.badRequest().body(
                manve.getConstraintViolations()
                        .stream()
                        .filter(constraintViolation -> !StringUtils.isEmpty(constraintViolation.getMessage()))
                        .distinct()
                        .map(constraintViolation -> {

                            StringBuilder message = new StringBuilder();

                            String msg = "";
                            try {
                                msg = JsonUtils.stringify(constraintViolation.getInvalidValue());
                            } catch (JsonProcessingException ignored) {

                            }

                            log.warn(message
                                    .append("Error '")
                                    .append(constraintViolation.getMessage())
                                    .append(" -> rejected value is '")
                                    .append(msg)
                                    .append("'")
                                    .toString());

                            return ICustomExceptionsHandler.buildErrorResponse(
                                    this.profile,
                                    ((ServletWebRequest) request).getRequest(),
                                    "ERRVALIDATION001",
                                    "Validation error ",
                                    message);
                        }).toList()
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e,
                                                             @Nullable Object body,
                                                             HttpHeaders headers,
                                                             HttpStatusCode status,
                                                             WebRequest request) {

        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute("javax.servlet.error.exception", e, 0);
        }

        if (e instanceof BindException be) {
            ErrorResponse response = getBindExceptionResponse(be.getBindingResult(), request);
            return ResponseEntity.badRequest().body(response);
        }

        return new ResponseEntity<>(
                ICustomExceptionsHandler.buildErrorResponse(
                        profile,
                        ((ServletWebRequest) request).getRequest(),
                        "INTSERVERERR",
                        "Internal server error",
                        e.getMessage()),
                status);
    }

    private List<String> getErrors(BindingResult bindingResult) {
        return bindingResult
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField().concat(": ").concat(Objects.requireNonNull(fieldError.getDefaultMessage())))
                .toList();
    }

    private ErrorResponse getBindExceptionResponse(BindingResult bindingResult, WebRequest request) {
        return getBindExceptionResponse(bindingResult, ((ServletWebRequest) request).getRequest());
    }

    private ErrorResponse getBindExceptionResponse(BindingResult bindingResult, HttpServletRequest request) {

        List<String> errs = getErrors(bindingResult);
        log.warn(String.join(", ", errs));

        return ICustomExceptionsHandler.buildErrorResponse(
                this.profile,
                request,
                "ERRVALIDATION001",
                "Validation error",
                errs);
    }
}
