package com.kduytran.paymentservice.entity.attrconverter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kduytran.paymentservice.entity.OrderDetailsInfo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class OrderDetailsInfoAttrConverter implements AttributeConverter<OrderDetailsInfo, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(OrderDetailsInfo attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException jpe) {
            log.warn("Cannot convert AppliedCouponsInfo into JSON");
            return null;
        }
    }

    @Override
    public OrderDetailsInfo convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, OrderDetailsInfo.class);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert JSON into AppliedCouponsInfo");
            return null;
        }
    }
}
