package edu.java.dao.dto;

import java.time.OffsetDateTime;
import lombok.Value;

@Value
public class ChatDTO {
    Long id;
    OffsetDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChatDTO)) {
            return false;
        }
        ChatDTO otherChatDTO = (ChatDTO) o;
        return id.equals(otherChatDTO.id) && isEqualOffsetDateTime(createdAt, otherChatDTO.createdAt);
    }

    private boolean isEqualOffsetDateTime(OffsetDateTime first, OffsetDateTime second) {
        return first.withNano(0).equals(second.withNano(0));
    }
}
