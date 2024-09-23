package com.kduytran.authmanagementservice.converter;

import com.kduytran.authmanagementservice.dto.UserDTO;
import com.kduytran.authmanagementservice.dto.UserRequestDTO;
import lombok.experimental.UtilityClass;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Arrays;

@UtilityClass
public class UserConverter {

    public static UserDTO convert(UserRepresentation representation, UserDTO dto) {
        if (dto == null) {
            dto = new UserDTO();
        }
        dto.setId(representation.getId());
        dto.setEmail(representation.getEmail());
        dto.setFullName(representation.getFirstName());
        dto.setUserName(representation.getUsername());
        return dto;
    }

    public static UserRepresentation convert(UserRequestDTO dto, UserRepresentation representation) {
        if (representation == null) {
            representation = new UserRepresentation();
        }
        // Create credential
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(dto.getPassword());

        // Setup user data
        representation.setEmail(dto.getEmail());
        representation.setFirstName(dto.getName());
        representation.setUsername(dto.getUsername());
        representation.setCredentials(Arrays.asList(credential));

        return representation;
    }

}
