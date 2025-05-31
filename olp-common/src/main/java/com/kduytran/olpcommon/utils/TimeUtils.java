package com.kduytran.olpcommon.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

    public static DateTimeFormatter DATE_FORMAT_YYYY_MM_DD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDateTime getFormattedDate(String dateString, DateTimeFormatter dateTimeFormatter) {
        LocalDate date = LocalDate.parse(dateString, dateTimeFormatter);
        return date.atStartOfDay();
    }
}
