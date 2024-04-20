package edu.java.controller;

import edu.java.exceptions.AlreadyRegisteredDataException;
import edu.java.response.ApiErrorResponse;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

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

    @ExceptionHandler(value = {AlreadyRegisteredDataException.class})
    public ResponseEntity<ApiErrorResponse> resourceAlreadyTrackedLinkException(
        AlreadyRegisteredDataException ex,
        WebRequest request
    ) {
        ApiErrorResponse errorDescription = ApiErrorResponse.builder()
            .description("Data already registered")
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDescription);
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
