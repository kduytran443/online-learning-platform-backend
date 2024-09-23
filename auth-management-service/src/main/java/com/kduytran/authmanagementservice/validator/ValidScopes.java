package com.kduytran.authmanagementservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ScopesValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidScopes {
    String message() default "The provided scope values are invalid." +
            "Please ensure all scope names are from the predefined list of valid scopes.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
