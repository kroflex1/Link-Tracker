package edu.java.utils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class TimeConvertor {
    public static OffsetDateTime convertEpochToOffsetDateTime(Long epochValue) {
        return OffsetDateTime.of(LocalDateTime.ofEpochSecond(epochValue, 0, ZoneOffset.UTC), ZoneOffset.UTC);
    }

    private TimeConvertor() {

    }
}
