package com.kakaopay.investment.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateParser {
    public static LocalDateTime getLocalDateTime(String currentDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(currentDate, dateTimeFormatter);
        return localDateTime;
    }
}
