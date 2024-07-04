package com.kduytran.classqueryservice.utils;

import lombok.experimental.UtilityClass;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class StreamUtil {

    public static <T> Stream<T> iterableToStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

}
