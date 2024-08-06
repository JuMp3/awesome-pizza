package com.example.awesome_pizza.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public interface ICustomExceptionsHandler {

    ResponseEntity<ErrorResponse> handleGenericException(Exception e, HttpServletRequest request);

    static ErrorResponse buildErrorResponse(String profile, HttpServletRequest request,
                                            String errorCode, String errorMessage, Object errorDetails) {

        ErrorResponse errorResponse = findFirstErrorResponse(errorDetails);
        if (errorResponse != null) {
            String innerErrorCode = errorResponse.getErrorCode();
            String innerErrorMessage = errorResponse.getErrorMessage();
            Object innerTechnicalErrorDetails = errorResponse.getTechnicalErrorDetails();
            if (StringUtils.isNotBlank(innerErrorCode)) {
                errorCode = innerErrorCode;
            }
            if (StringUtils.isNotBlank(innerErrorMessage)) {
                errorMessage = innerErrorMessage;
            }
            if (innerTechnicalErrorDetails != null) {
                errorDetails = innerTechnicalErrorDetails;
            }
        }
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                .uri(request.getRequestURI())
                .pod(!"prod".equalsIgnoreCase(profile) ? request.getLocalName() : null)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .technicalErrorDetails(!"prod".equalsIgnoreCase(profile) ? errorDetails : null)
                .build();
    }

    private static ErrorResponse findFirstErrorResponse(Object errorDetails) {

        if (errorDetails instanceof String value) {
            return tryParse(value);
        } else if (errorDetails instanceof List<?> list) {
            for (Object obj : list) {
                if (!(obj instanceof String)) {
                    continue;
                }
                ErrorResponse errorResponse = tryParse((String) obj);
                if (errorResponse != null) {
                    return errorResponse;
                }
            }
        }
        return null;
    }

    private static ErrorResponse tryParse(String json) {

        ErrorResponse out;
        ObjectMapper mapper = new ObjectMapper();

        // Try standard error response first...
        try {
            out = mapper.readValue(json, ErrorResponse.class);
        } catch (Exception e) {
            // ... then generic response
            out = new ErrorResponse();
        }

        return out;
    }
}
