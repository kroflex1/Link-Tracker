package edu.java.dao.dto;

import edu.java.utils.TimeManager;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Value;

@Value
public class LinkDTO {
    URI link;
    OffsetDateTime createdTime;
    OffsetDateTime lastTimeUpdate;

    @Override
    @SuppressWarnings("hashCode")
    public boolean equals(Object o) {
        if (!(o instanceof LinkDTO otherLinkDTO)) {
            return false;
        }
        return link.equals(otherLinkDTO.link)
            && TimeManager.isEqualOffsetDateTime(createdTime, otherLinkDTO.createdTime)
            && TimeManager.isEqualOffsetDateTime(lastTimeUpdate, otherLinkDTO.lastTimeUpdate);
    }
}
