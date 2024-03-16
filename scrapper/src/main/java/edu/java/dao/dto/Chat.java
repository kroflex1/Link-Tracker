package edu.java.dao.dto;

import edu.java.utils.TimeManager;
import java.time.OffsetDateTime;
import lombok.Value;

@Value
@SuppressWarnings("hashCode")
public class Chat {
    Long chatId;
    OffsetDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Chat otherChat)) {
            return false;
        }
        return chatId.equals(otherChat.chatId) && TimeManager.isEqualOffsetDateTime(createdAt, otherChat.createdAt);
    }

}
