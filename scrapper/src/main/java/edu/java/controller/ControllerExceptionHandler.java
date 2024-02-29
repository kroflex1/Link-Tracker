package edu.java.controller;

import edu.java.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<ApiErrorResponse> resourceIllegalArgumentException(
        IllegalArgumentException ex,
        WebRequest request
    ) {
        ApiErrorResponse errorDescription = ApiErrorResponse.builder()
            .description("Invalid request parameters")
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDescription);
    }

    @ExceptionHandler(value = {MalformedURLException.class, URISyntaxException.class})
    public ResponseEntity<ApiErrorResponse> resourceInvalidLinkException(
        Exception ex,
        WebRequest request
    ) {
        ApiErrorResponse errorDescription = ApiErrorResponse.builder()
            .description("Invalid link format")
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDescription);
    }
}
