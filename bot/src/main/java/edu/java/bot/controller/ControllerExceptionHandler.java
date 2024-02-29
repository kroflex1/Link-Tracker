package edu.java.bot.controller;

import edu.java.bot.dto.response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiErrorResponse resourceIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return ApiErrorResponse.builder()
            .description("Invalid request parameters")
            .exceptionName(ex.getClass().getName())
            .exceptionMessage(ex.getMessage())
            .build();
    }
}
