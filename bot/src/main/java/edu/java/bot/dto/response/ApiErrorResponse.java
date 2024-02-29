package edu.java.bot.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiErrorResponse {
    private String description;
    private String code;
    private String exceptionName;
    private String exceptionMessage;
}
