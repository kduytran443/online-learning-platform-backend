package com.kduytran.authmanagementservice.service.impl;

import com.kduytran.authmanagementservice.converter.UserConverter;
import com.kduytran.authmanagementservice.dto.UserDTO;
import com.kduytran.authmanagementservice.dto.UserRegisterRequestDTO;
import com.kduytran.authmanagementservice.service.IAuthManagementService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthManagementServiceImpl implements IAuthManagementService {
    private final Keycloak keycloak;
    private final String realm;

    public AuthManagementServiceImpl(Keycloak keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    @Override
    public boolean registerUser(UserRegisterRequestDTO dto) {

        return false;
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        List<UserRepresentation> search = keycloak.realm(realm).users().search(username);
        if (search.isEmpty()) {
            // TODO: throw error
        }
        return UserConverter.convert(search.get(0), new UserDTO());
    }

}
