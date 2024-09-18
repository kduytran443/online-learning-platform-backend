package com.kduytran.authmanagementservice.service.impl;

import com.kduytran.authmanagementservice.converter.UserConverter;
import com.kduytran.authmanagementservice.dto.UserDTO;
import com.kduytran.authmanagementservice.dto.UserRequestDTO;
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
    public boolean removeUser(String userId) {
        UsersResource resource = keycloak.realm(realm).users();
        Response response = resource.delete(userId);
        return response.getStatus() == HttpStatus.OK.value();
    }

    @Override
    public void updateUser(UserRequestDTO dto) {
        keycloak.realm(realm).users().get(dto.getId()).update(UserConverter.convert(dto, new UserRepresentation()));
    }

    @Override
    public int registerUser(UserRequestDTO dto) {
        // Create users resource
        UsersResource usersResource = keycloak.realm(realm).users();

        // Create user
        Response response = usersResource.create(UserConverter.convert(dto, new UserRepresentation()));

        return response.getStatus();
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
