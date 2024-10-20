package com.kduytran.userservice.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class LogUtils {

    public static String withCorrelation(String CorrelationId, String message) {
        return String.format("[Correlation id=%s] %s", CorrelationId, message);
    }

}
