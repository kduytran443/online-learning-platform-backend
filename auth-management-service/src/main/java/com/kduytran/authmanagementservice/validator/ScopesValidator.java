package com.kduytran.authmanagementservice.validator;

import com.kduytran.authmanagementservice.constant.ResourceScope;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;

import java.util.Set;
import java.util.stream.Collectors;

public class ScopesValidator implements ConstraintValidator<ValidScopes, Set<String>> {
    private Set<String> validScopes;

    @Override
    public void initialize(ValidScopes constraintAnnotation) {
        validScopes = ResourceScope.getAllScopes().stream()
                .map(ScopeRepresentation::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Set<String> scopes, ConstraintValidatorContext context) {
        if (scopes == null || scopes.isEmpty()) {
            return false;
        }
        return scopes.stream().allMatch(scope -> validScopes.contains(scope));
    }

}
