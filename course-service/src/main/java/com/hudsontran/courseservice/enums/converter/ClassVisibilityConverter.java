package com.hudsontran.courseservice.enums.converter;

import com.hudsontran.courseservice.enums.ClassVisibility;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ClassVisibilityConverter
        implements AttributeConverter<ClassVisibility, String> {

    @Override
    public String convertToDatabaseColumn(ClassVisibility attribute) {
        return attribute == null
                ? null
                : attribute.name();
    }

    @Override
    public ClassVisibility convertToEntityAttribute(String dbData) {
        return dbData == null
                ? null
                : ClassVisibility.valueOf(dbData);
    }
}
