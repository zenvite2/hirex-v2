package com.ptit.websocket.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeUtil {

    public static LocalDateTime convertLongToLocalDateTime(long millis) {
        ZoneId hoChiMinhZone = ZoneId.of("Asia/Ho_Chi_Minh");
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), hoChiMinhZone);
    }
}
