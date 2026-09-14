package com.hudsontran.courseservice.enums.converter;

import com.hudsontran.courseservice.enums.ClassStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ClassStatusConverter
        implements AttributeConverter<ClassStatus, String> {

    @Override
    public String convertToDatabaseColumn(ClassStatus attribute) {
        return attribute == null
                ? null
                : attribute.name();
    }

    @Override
    public ClassStatus convertToEntityAttribute(String dbData) {
        return dbData == null
                ? null
                : ClassStatus.valueOf(dbData);
    }
}
