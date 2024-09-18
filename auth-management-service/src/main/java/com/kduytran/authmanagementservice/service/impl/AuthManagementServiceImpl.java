package com.kduytran.authmanagementservice.service.impl;

import com.kduytran.authmanagementservice.converter.UserConverter;
import com.kduytran.authmanagementservice.dto.UserDTO;
import com.kduytran.authmanagementservice.dto.UserRequestDTO;
import com.kduytran.authmanagementservice.exception.KeyCloakActionFailedException;
import com.kduytran.authmanagementservice.exception.UserNotFoundException;
import com.kduytran.authmanagementservice.service.IAuthManagementService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
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
    public void removeUser(String userId) {
        try (keycloak) {
            UsersResource resource = keycloak.realm(realm).users();
            Response response = resource.delete(userId);

            if (!isSuccessStatus(response.getStatus())) {
                throw new KeyCloakActionFailedException(response.readEntity(String.class));
            }
        }
    }

    @Override
    public void updateUser(UserRequestDTO dto) {
        try (keycloak) {
            keycloak.realm(realm).users().get(dto.getId())
                    .update(UserConverter.convert(dto, new UserRepresentation()));
        }
    }

    @Override
    public void registerUser(UserRequestDTO dto) {
        try (keycloak) {
            // Create users resource
            UsersResource usersResource = keycloak.realm(realm).users();

            // Create user
            Response response = usersResource.create(UserConverter.convert(dto, new UserRepresentation()));
            keycloak.close();

            if (!isSuccessStatus(response.getStatus())) {
                throw new KeyCloakActionFailedException(response.readEntity(String.class));
            }
        }
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        try (keycloak) {
            List<UserRepresentation> search = keycloak.realm(realm).users().search(username);
            if (search.isEmpty()) {
                throw new UserNotFoundException("No user found with the username: " + username);
            }
            return UserConverter.convert(search.get(0), new UserDTO());
        }
    }

    private boolean isSuccessStatus(int status) {
        return HttpStatus.Series.valueOf(status).compareTo(HttpStatus.Series.SUCCESSFUL) == 0;
    }

}
