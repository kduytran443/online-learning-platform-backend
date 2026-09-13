package com.hudsontran.courseservice.enums.converter;

import com.hudsontran.courseservice.enums.CourseStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CourseStatusConverter
        implements AttributeConverter<CourseStatus, String> {

    @Override
    public String convertToDatabaseColumn(CourseStatus attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public CourseStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CourseStatus.valueOf(dbData);
    }
}
