package com.kduytran.paymentservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@UtilityClass
public class StreamUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static <T> void logEvent(Logger logger, T obj) {
        logger.info(String.format("Processed %s: Action = %s, Code = %s, Name = %s"));
    }


    public static <T> List<T> findAllFromStore(ReadOnlyKeyValueStore<String, T> store) {
        if (store == null) {
            return Collections.emptyList();
        }
        List<T> list = new ArrayList<>();
        try(var keyValueIterator = store.all()) {
            while (keyValueIterator.hasNext()) {
                var pair = keyValueIterator.next();
                list.add(pair.value);
            }
        }
        return list;
    }

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
