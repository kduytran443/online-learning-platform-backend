package com.hudsontran.courseservice.enums.converter;

import com.hudsontran.courseservice.enums.AccessPolicy;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AccessPolicyConverter
        implements AttributeConverter<AccessPolicy, String> {

    @Override
    public String convertToDatabaseColumn(AccessPolicy attribute) {
        return attribute == null
                ? null
                : attribute.name();
    }

    @Override
    public AccessPolicy convertToEntityAttribute(String dbData) {
        return dbData == null
                ? null
                : AccessPolicy.valueOf(dbData);
    }
}
