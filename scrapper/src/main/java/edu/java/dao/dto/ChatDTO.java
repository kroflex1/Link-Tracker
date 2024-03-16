package edu.java.dao.dto;

import edu.java.utils.TimeManager;
import java.time.OffsetDateTime;
import lombok.Value;

@Value
@SuppressWarnings("hashCode")
public class ChatDTO {
    Long id;
    OffsetDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChatDTO otherChatDTO)) {
            return false;
        }
        return id.equals(otherChatDTO.id) && TimeManager.isEqualOffsetDateTime(createdAt, otherChatDTO.createdAt);
    }

}
