package com.kduytran.authmanagementservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum ResourceScope {
    EDIT(new ScopeRepresentation("edit")),
    VIEW(new ScopeRepresentation("view")),
    DELETE(new ScopeRepresentation("delete"));

    private ScopeRepresentation scope;

    public static Set<ScopeRepresentation> getAllScopes() {
        return Arrays.stream(values())
                .map(ResourceScope::getScope)
                .collect(Collectors.toSet());
    }

}
