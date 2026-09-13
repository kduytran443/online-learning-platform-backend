package com.hudsontran.courseservice.enums.converter;

import com.hudsontran.courseservice.enums.CategoryVisibility;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CategoryVisibilityConverter
        implements AttributeConverter<CategoryVisibility, String> {

    @Override
    public String convertToDatabaseColumn(CategoryVisibility attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public CategoryVisibility convertToEntityAttribute(String dbData) {
        return dbData == null ? null : CategoryVisibility.valueOf(dbData);
    }
}
