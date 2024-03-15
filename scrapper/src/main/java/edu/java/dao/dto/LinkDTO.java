package edu.java.dao.dto;

import edu.java.utils.TimeManager;
import lombok.Value;
import java.net.URI;
import java.time.OffsetDateTime;

@Value
public class LinkDTO {
    URI link;
    OffsetDateTime createdTime;
    OffsetDateTime lastTimeUpdate;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LinkDTO otherLinkDTO)) {
            return false;
        }
        return link.equals(otherLinkDTO.link) &&
            TimeManager.isEqualOffsetDateTime(createdTime, otherLinkDTO.createdTime) &&
            TimeManager.isEqualOffsetDateTime(lastTimeUpdate, otherLinkDTO.lastTimeUpdate);
    }
}
