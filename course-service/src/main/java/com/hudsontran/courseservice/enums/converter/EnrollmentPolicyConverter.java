package com.hudsontran.courseservice.enums.converter;

import com.hudsontran.courseservice.enums.EnrollmentPolicy;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EnrollmentPolicyConverter
        implements AttributeConverter<EnrollmentPolicy, String> {

    @Override
    public String convertToDatabaseColumn(EnrollmentPolicy attribute) {
        return attribute == null
                ? null
                : attribute.name();
    }

    @Override
    public EnrollmentPolicy convertToEntityAttribute(String dbData) {
        return dbData == null
                ? null
                : EnrollmentPolicy.valueOf(dbData);
    }
}
