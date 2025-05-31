package com.kduytran.orderservice.entity.attrconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kduytran.orderservice.entity.UserInfo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class UserInfoAttributeConverter implements AttributeConverter<UserInfo, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(UserInfo attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException jpe) {
            log.warn("Cannot convert UserInfo into JSON");
            return null;
        }
    }

    @Override
    public UserInfo convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, UserInfo.class);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert JSON into UserInfo");
            return null;
        }
    }

}
