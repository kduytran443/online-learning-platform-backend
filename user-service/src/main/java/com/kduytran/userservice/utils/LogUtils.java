package com.kduytran.userservice.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LogUtils {

    public static String getLogDebugFormat(String message) {
        return String.format("Transaction ID: {} - %s", message);
    }

}
