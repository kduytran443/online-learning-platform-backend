package com.hudsontran.courseservice.enums.converter;

import com.hudsontran.courseservice.enums.CourseVisibility;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CourseVisibilityConverter
        implements AttributeConverter<CourseVisibility, String> {

    @Override
    public String convertToDatabaseColumn(CourseVisibility attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public CourseVisibility convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CourseVisibility.valueOf(dbData);
    }
}
