package com.kduytran.authmanagementservice.utils;

import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public class TimeUtils {

    public static Instant getExpiredTime(long second) {
        return Instant.now().plusSeconds(second);
    }
}
