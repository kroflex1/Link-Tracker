package edu.java.dao.dto;

import lombok.Value;
import java.net.URI;
import java.time.OffsetDateTime;

@Value
public class LinkDTO {
    URI link;
    OffsetDateTime createdTime;
    OffsetDateTime lastTimeUpdate;
}
