package com.kduytran.memberservice.entity.attrconverter;

import com.kduytran.memberservice.entity.ClassRole;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ClassRoleAttrConverter implements AttributeConverter<ClassRole, String> {
    @Override
    public String convertToDatabaseColumn(ClassRole attribute) {
        return attribute.getCode();
    }

    @Override
    public ClassRole convertToEntityAttribute(String dbData) {
        return ClassRole.of(dbData);
    }
}
