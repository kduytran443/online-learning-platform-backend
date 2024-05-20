package com.kduytran.gatewayserver.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathUtils {

    public static final String CONTEXT_PATH = "olp";

    public static String getPathWithContextPath(final String path) {
        return String.format("/%s/%s", CONTEXT_PATH, path);
    }

    public static String getServicePath(String resource) {
        return getPathWithContextPath(String.format("{versioning:v\\d{1,2}}/%s/**", resource));
    }

    public static String getRewriteSourcePath(String resource) {
        return getPathWithContextPath(String.format("(?<versioning>v\\d{1,2})/%s/(?<segment>.*)", resource));
    }

    public static String getRewriteDestinationPath(String resource) {
        return String.format("/api/${versioning}/%s/${segment}", resource);
    }

    public static String getUri(String serviceName) {
        return String.format("lb://%s", serviceName.toUpperCase());
    }

}
