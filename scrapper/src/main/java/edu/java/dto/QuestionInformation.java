package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import edu.java.utils.TimeConvertor;
import io.micrometer.common.lang.Nullable;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class QuestionInformation {
    @JsonSetter("question_id") private long id;
    @JsonSetter("title") private String text;
    private OffsetDateTime creationDate;
    private OffsetDateTime lastActivityDate;

    @Nullable private AdditionalInformation lastAnswer;
    @Nullable private AdditionalInformation lastComment;

    @JsonSetter("creation_date")
    public void setCreationDate(long epochValue) {
        creationDate = TimeConvertor.convertEpochToOffsetDateTime(epochValue);
    }

    @JsonSetter("last_activity_date")
    public void setLastActivityDate(long epochValue) {
        lastActivityDate = TimeConvertor.convertEpochToOffsetDateTime(epochValue);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class AdditionalInformation {
        private OffsetDateTime creationDate;
        private String ownerName;
        private String text;
        private String link;
    }
}
