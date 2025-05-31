package com.kduytran.olpcommon.validation;

public class ValidationRule {
    private String fieldName;
    private String message;
    private boolean isUpdate;

    public ValidationRule() {
    }

    private ValidationRule(Builder builder) {
        this.fieldName = builder.fieldName;
        this.message = builder.message;
        this.isUpdate = builder.isUpdate;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String fieldName;
        private String message;
        private boolean isUpdate;

        public Builder fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder isUpdate(boolean isUpdate) {
            this.isUpdate = isUpdate;
            return this;
        }

        public ValidationRule build() {
            return new ValidationRule(this);
        }
    }
}
