package com.kduytran.notificationservice.utils;

import com.kduytran.notificationservice.constant.AttributeConstant;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class TimeUtils {

    public static String FULL_DATETIME_FORMAT = "dd MMMM yyyy 'at' hh:mm a";

    public static String getFormattedDate(LocalDateTime dateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedDateTime = dateTime.format(formatter);
        return formattedDateTime;
    }

    public static String getFormattedDate(Object dateTimeObject, String format) {
        LocalDateTime originalDate = LocalDateTime.parse(String.valueOf(dateTimeObject));
        return getFormattedDate(originalDate, format);
    }

}
