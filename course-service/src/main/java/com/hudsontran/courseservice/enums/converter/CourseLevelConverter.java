package com.hudsontran.courseservice.enums.converter;

import com.hudsontran.courseservice.enums.CourseLevel;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CourseLevelConverter
        implements AttributeConverter<CourseLevel, String> {

    @Override
    public String convertToDatabaseColumn(CourseLevel attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public CourseLevel convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CourseLevel.valueOf(dbData);
    }
}
