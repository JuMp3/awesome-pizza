package com.example.awesome_pizza.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private long timestamp;
    private String uri;
    private String pod;
    private String errorCode;
    private String errorMessage;
    private Object technicalErrorDetails;
}
