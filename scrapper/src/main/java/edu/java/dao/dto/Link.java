package edu.java.dao.dto;

import edu.java.utils.TimeManager;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Value;

@Value
public class Link {
    URI url;
    OffsetDateTime createdTime;
    OffsetDateTime lastCheckTime;
    OffsetDateTime lastActivityTime;

    @Override
    @SuppressWarnings("hashCode")
    public boolean equals(Object o) {
        if (!(o instanceof Link otherLink)) {
            return false;
        }
        return url.equals(otherLink.url)
            && TimeManager.isEqualOffsetDateTime(createdTime, otherLink.createdTime)
            && TimeManager.isEqualOffsetDateTime(lastCheckTime, otherLink.lastCheckTime)
            && TimeManager.isEqualOffsetDateTime(lastActivityTime, otherLink.lastActivityTime);
    }
}
