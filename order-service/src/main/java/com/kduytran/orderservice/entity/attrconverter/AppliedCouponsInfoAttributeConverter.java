package com.kduytran.orderservice.entity.attrconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kduytran.orderservice.entity.AppliedCouponsInfo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter
public class AppliedCouponsInfoAttributeConverter implements AttributeConverter<AppliedCouponsInfo, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AppliedCouponsInfo attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException jpe) {
            log.warn("Cannot convert AppliedCouponsInfo into JSON");
            return null;
        }
    }

    @Override
    public AppliedCouponsInfo convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, AppliedCouponsInfo.class);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert JSON into AppliedCouponsInfo");
            return null;
        }
    }

}
