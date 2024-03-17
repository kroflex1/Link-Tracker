package edu.java.dao.dto;

import edu.java.utils.TimeManager;
import java.time.OffsetDateTime;
import lombok.Value;

@Value
public class ChatDTO {
    Long chatId;
    OffsetDateTime createdAt;

    @Override
    @SuppressWarnings("EqualsHashCode")
    public boolean equals(Object o) {
        if (!(o instanceof ChatDTO otherChat)) {
            return false;
        }
        return chatId.equals(otherChat.chatId) && TimeManager.isEqualOffsetDateTime(createdAt, otherChat.createdAt);
    }

}
