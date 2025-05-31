package com.kduytran.olpcommon.validation;

public interface FieldValueExists {
    boolean exists(Object fieldValue, String fieldName);
    boolean exists(Object fieldValue, String fieldName, Object id);
}
