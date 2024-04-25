package com.kduytran.userservice.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class TimeUtils {

    public static boolean isExpired(LocalDateTime expiredDate) {
        if (expiredDate == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expiredDate);
    }

    public static LocalDateTime getExpiredTime(long second) {
        return LocalDateTime.now().plusSeconds(second);
    }

}
