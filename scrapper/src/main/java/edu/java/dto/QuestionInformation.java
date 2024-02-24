package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class QuestionInformation {
    private Long id;
    private OffsetDateTime creationDate;
    private OffsetDateTime lastActivityDate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Answer lastAnswer;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Comment lasComment;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class Answer {
        private OffsetDateTime creationDate;
        private String ownerName;
        private String text;
        private String link;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Comment {
        private OffsetDateTime creationDate;
        private String ownerName;
        private String text;
        private String link;
    }
}
