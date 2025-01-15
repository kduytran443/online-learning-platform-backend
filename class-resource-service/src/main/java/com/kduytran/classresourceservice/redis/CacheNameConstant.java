package com.kduytran.classresourceservice.redis;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CacheNameConstant {

    public static final String TOPIC_CACHE = "class-resource-service/topic";
    public static final String LESSON_CACHE = "class-resource-service/lesson";
    public static final String LESSON_CONTENT_CACHE = "class-resource-service/lesson-content";
}
