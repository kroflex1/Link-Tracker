package edu.java.dao.dto;

import edu.java.utils.TimeManager;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Value;

@Value
public class Link {
    URI uri;
    OffsetDateTime createdTime;
    OffsetDateTime lastTimeUpdate;

    @Override
    @SuppressWarnings("hashCode")
    public boolean equals(Object o) {
        if (!(o instanceof Link otherLink)) {
            return false;
        }
        return uri.equals(otherLink.uri)
            && TimeManager.isEqualOffsetDateTime(createdTime, otherLink.createdTime)
            && TimeManager.isEqualOffsetDateTime(lastTimeUpdate, otherLink.lastTimeUpdate);
    }
}
