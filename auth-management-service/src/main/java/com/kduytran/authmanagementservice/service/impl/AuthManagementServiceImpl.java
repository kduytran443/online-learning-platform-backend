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
import java.util.function.Supplier;

@Service
public class AuthManagementServiceImpl implements IAuthManagementService {
    private final Supplier<Keycloak> keycloak;
    private final String realm;

    public AuthManagementServiceImpl(Supplier<Keycloak> keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    @Override
    public void removeUser(String userId) {
        Keycloak keycloakInstance = keycloak.get();
        try (keycloakInstance) {
            UsersResource resource = keycloakInstance.realm(realm).users();
            Response response = resource.delete(userId);

            if (!isSuccessStatus(response.getStatus())) {
                throw new KeyCloakActionFailedException(response.readEntity(String.class));
            }
        }
    }

    @Override
    public void updateUser(String id, UserRequestDTO dto) {
        Keycloak keycloakInstance = keycloak.get();
        try (keycloakInstance) {
            keycloakInstance.realm(realm).users().get(id)
                    .update(UserConverter.convert(dto, new UserRepresentation()));
        }
    }

    @Override
    public void registerUser(UserRequestDTO dto) {
        Keycloak keycloakInstance = keycloak.get();
        try (keycloakInstance) {
            // Create users resource
            UsersResource usersResource = keycloakInstance.realm(realm).users();

            UserRepresentation userRepresentation = UserConverter.convert(dto, new UserRepresentation());
            userRepresentation.setEnabled(true);
            userRepresentation.setEmailVerified(true);

            // Create user
            Response response = usersResource.create(userRepresentation);

            if (!isSuccessStatus(response.getStatus())) {
                throw new KeyCloakActionFailedException(response.readEntity(String.class));
            }
        }
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        Keycloak keycloakInstance = keycloak.get();
        try (keycloakInstance) {
            List<UserRepresentation> search = keycloakInstance.realm(realm).users().search(username);
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
