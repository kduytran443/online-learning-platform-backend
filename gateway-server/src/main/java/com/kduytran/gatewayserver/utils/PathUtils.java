package com.kduytran.gatewayserver.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathUtils {

    public static final String CONTEXT_PATH = "olp";

    public static String getPathWithContextPath(final String path) {
        return String.format("/%s/%s", CONTEXT_PATH, path);
    }

}
