package edu.java.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder
public class ApiErrorResponse {
    String description;
    String exceptionName;
    String exceptionMessage;
}