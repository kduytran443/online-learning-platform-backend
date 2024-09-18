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
        dto.setEmail(representation.getEmail());
        dto.setFirstname(representation.getFirstName());
        dto.setLastName(representation.getLastName());
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
        representation.setId(dto.getId());
        representation.setEmail(dto.getEmail());
        representation.setLastName(dto.getLastName());
        representation.setFirstName(dto.getFirstname());
        representation.setUsername(dto.getUserName());
        representation.setCredentials(Arrays.asList(credential));

        return representation;
    }

}
