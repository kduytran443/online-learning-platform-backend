package com.kduytran.userservice.entity.attrconverter;

import com.kduytran.userservice.entity.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserStatusAttributeConverter implements AttributeConverter<UserStatus, String> {
    /**
     * Converts the value stored in the entity attribute into the
     * data representation to be stored in the database.
     *
     * @param attribute the entity attribute value to be converted
     * @return the converted data to be stored in the database
     * column
     */
    @Override
    public String convertToDatabaseColumn(UserStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
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
    public UserStatus convertToEntityAttribute(String dbData) {
        return UserStatus.of(dbData);
    }
}
