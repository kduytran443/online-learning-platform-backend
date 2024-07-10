package com.kduytran.classqueryservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kduytran.classqueryservice.dto.CategoryDTO;
import com.kduytran.classqueryservice.event.CategoryEvent;
import lombok.experimental.UtilityClass;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class StreamUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtils.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T mapValue(String value, Class<T> tClass) {
        try {
            return objectMapper.readValue(value, tClass);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
            try {
                return tClass.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                LOGGER.error(ex.getMessage());
                return null;
            }
        }
    }

    public static <T> Stream<T> iterableToStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> Stream<KeyValue<String, T>> toStream(ReadOnlyKeyValueStore<String, T> store) {
        try (KeyValueIterator<String, T> all = store.all()) {
            Iterable<KeyValue<String, T>> iterable = () -> all;
            return StreamSupport.stream(iterable.spliterator(), false).onClose(all::close);
        }
    }

}
