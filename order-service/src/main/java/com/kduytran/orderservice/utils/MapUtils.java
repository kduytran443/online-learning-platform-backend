package com.kduytran.orderservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class MapUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static <T> T mapValue(String value, Class<T> tClass) {
        try {
            return objectMapper.readValue(value, tClass);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            try {
                return tClass.getDeclaredConstructor().newInstance();
            } catch (Exception ex) {
                log.error(ex.getMessage());
                return null;
            }
        }
    }

}
