package me.devld.tour.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    private TimeUtil() {
    }

    public static final ZoneId ZONE_CHINA = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter FORMATTER_NORMAL_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_NORMAL_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static long parseNormalTime(String str, ZoneId zoneId) {
        return LocalDateTime.parse(str, FORMATTER_NORMAL_TIME).atZone(zoneId).toInstant().toEpochMilli();
    }

    public static long parseNormalDate(String str, ZoneId zoneId) {
        return LocalDate.parse(str, FORMATTER_NORMAL_DATE).atStartOfDay().atZone(zoneId).toInstant().toEpochMilli();
    }

}
