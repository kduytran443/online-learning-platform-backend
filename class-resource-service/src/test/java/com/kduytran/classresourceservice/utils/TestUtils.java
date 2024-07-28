package com.kduytran.classresourceservice.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {
    public static String URL_PATTERN = "%s/%s";
    public static String TOPIC_CONTROLLER_URL = "/api/v1/topics";
    public static String LESSON_CONTROLLER_URL = "/api/v1/lessons";

    public static String getUrl(@NonNull String url, @NonNull String... pathSegments) {
        if (pathSegments.length == 0) {
            return url;
        }
        String path = String.join("/", pathSegments);
        return String.format(URL_PATTERN, url, path);
    }

    public static String getTopicUrl(@NonNull String... pathSegments) {
        return getUrl(TOPIC_CONTROLLER_URL, pathSegments);
    }

    public static String getLessonUrl(@NonNull String... pathSegments) {
        return getUrl(LESSON_CONTROLLER_URL, pathSegments);
    }

}
