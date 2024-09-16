package com.kduytran.authmanagementservice.converter;

import com.kduytran.authmanagementservice.dto.UserDTO;
import lombok.experimental.UtilityClass;
import org.keycloak.representations.idm.UserRepresentation;

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

}
