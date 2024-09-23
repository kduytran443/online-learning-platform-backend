package com.kduytran.authmanagementservice.converter;

import com.kduytran.authmanagementservice.dto.ScopePermissionDTO;
import lombok.experimental.UtilityClass;
import org.keycloak.representations.idm.authorization.ScopePermissionRepresentation;

@UtilityClass
public class ScopePermissionConverter {

    public static ScopePermissionDTO convert(ScopePermissionRepresentation scopePermission, ScopePermissionDTO dto) {
        if (dto == null) {
            dto = new ScopePermissionDTO();
        }
        dto.setScopes(scopePermission.getScopes());
        dto.setResources(scopePermission.getResources());
        dto.setDescription(scopePermission.getDescription());
        dto.setName(scopePermission.getName());
        dto.setUserId(scopePermission.getOwner());
        return dto;
    }

}
