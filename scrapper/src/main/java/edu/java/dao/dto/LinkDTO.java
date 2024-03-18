package edu.java.dao.dto;

import edu.java.utils.TimeManager;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Value;

@Value
public class LinkDTO {
    URI url;
    OffsetDateTime createdTime;
    OffsetDateTime lastCheckTime;
    OffsetDateTime lastActivityTime;

    @Override
    @SuppressWarnings("EqualsHashCode")
    public boolean equals(Object o) {
        if (!(o instanceof LinkDTO otherLink)) {
            return false;
        }
        return url.equals(otherLink.url)
            && TimeManager.isEqualOffsetDateTime(createdTime, otherLink.createdTime)
            && TimeManager.isEqualOffsetDateTime(lastCheckTime, otherLink.lastCheckTime)
            && TimeManager.isEqualOffsetDateTime(lastActivityTime, otherLink.lastActivityTime);
    }
}
