package edu.java.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimeManager {
    public static OffsetDateTime convertEpochToOffsetDateTime(Long epochValue) {
        return OffsetDateTime.of(LocalDateTime.ofEpochSecond(epochValue, 0, ZoneOffset.UTC), ZoneOffset.UTC);
    }

    public static OffsetDateTime convertTimestampToOffsetDateTime(Timestamp timestamp) {
        return timestamp.toInstant().atOffset(ZoneOffset.UTC);
    }

    public static Timestamp convertOffsetDateTimeToTimestamp(OffsetDateTime offsetDateTime) {
        return Timestamp.valueOf(offsetDateTime.toLocalDateTime());
    }

    public static boolean isEqualOffsetDateTime(OffsetDateTime first, OffsetDateTime second) {
        ZonedDateTime firstTime = first.atZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime secondTime = second.atZoneSameInstant(ZoneOffset.UTC);
        return firstTime.withNano(0).equals(secondTime.withNano(0));
    }

    private TimeManager() {

    }
}
