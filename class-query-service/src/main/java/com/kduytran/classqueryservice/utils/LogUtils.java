package com.kduytran.classqueryservice.utils;

import com.kduytran.classqueryservice.event.CategoryEvent;
import com.kduytran.classqueryservice.event.ClassEvent;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;

@UtilityClass
public class LogUtils {

    public static String CATEGORY_EVENT_FORMAT = "Processed %s: Action = %s, Id = %s, Code = %s, Name = %s";
    public static String CLASS_EVENT_FORMAT = "Processed %s: Action = %s, Id = %s, Name = %s";

    public static void logEvent(Logger logger, CategoryEvent event) {
        logger.info(String.format(CATEGORY_EVENT_FORMAT,
                event.getClass().getName(),
                event.getAction(),
                event.getCode(),
                event.getName())
        );
    }

    public static void logEvent(Logger logger, ClassEvent event) {
        logger.info(String.format(CLASS_EVENT_FORMAT,
                event.getClass().getName(),
                event.getAction(),
                event.getId(),
                event.getName())
        );
    }

}
