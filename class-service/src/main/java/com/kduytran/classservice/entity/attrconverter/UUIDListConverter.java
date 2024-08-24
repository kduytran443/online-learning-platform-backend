package com.kduytran.classservice.entity.attrconverter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Converter
public class UUIDListConverter implements AttributeConverter<List<UUID>, UUID[]> {

    /**
     * Converts the value stored in the entity attribute into the
     * data representation to be stored in the database.
     *
     * @param attribute the entity attribute value to be converted
     * @return the converted data to be stored in the database
     * column
     */
    @Override
    public UUID[] convertToDatabaseColumn(List<UUID> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        return attribute.toArray(new UUID[0]);
    }

    /**
     * Converts the data stored in the database column into the
     * value to be stored in the entity attribute.
     * Note that it is the responsibility of the converter writer to
     * specify the correct <code>dbData</code> type for the corresponding
     * column for use by the JDBC driver: i.e., persistence providers are
     * not expected to do such type conversion.
     *
     * @param dbData the data from the database column to be
     *               converted
     * @return the converted value to be stored in the entity
     * attribute
     */
    @Override
    public List<UUID> convertToEntityAttribute(UUID[] dbData) {
        if (dbData == null) {
            return null;
        }
        return Arrays.asList(dbData);
    }

}
