package com.kduytran.authmanagementservice.service.impl;

import com.kduytran.authmanagementservice.constant.ResourceScope;
import com.kduytran.authmanagementservice.converter.ScopePermissionConverter;
import com.kduytran.authmanagementservice.converter.UserConverter;
import com.kduytran.authmanagementservice.dto.*;
import com.kduytran.authmanagementservice.exception.KeyCloakActionFailedException;
import com.kduytran.authmanagementservice.exception.UserNotFoundException;
import com.kduytran.authmanagementservice.service.IAuthManagementService;
import com.kduytran.authmanagementservice.util.KeyCloakUtils;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.*;
import org.keycloak.authorization.client.resource.PermissionResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.keycloak.representations.idm.authorization.ScopePermissionRepresentation;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class AuthManagementServiceImpl implements IAuthManagementService {
    private final Supplier<Keycloak> keycloak;
    private final String realm;
    private final String clientId;

    public AuthManagementServiceImpl(Supplier<Keycloak> keycloak, @Value("${keycloak.realm}") String realm,
                                     @Value("${keycloak.resource}") String clientId) {
        this.keycloak = keycloak;
        this.realm = realm;
        this.clientId = clientId;
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
            UserRepresentation userRepresentation = UserConverter.convert(dto, new UserRepresentation());
            userRepresentation.setId(id);
            keycloakInstance.realm(realm).users().get(id).update(userRepresentation);
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

    @Override
    public void createResource(ResourceRequestDTO dto) {
        Keycloak keycloakInstance = keycloak.get();
        try (keycloakInstance) {
            ResourceRepresentation resource = new ResourceRepresentation();
            resource.setName(KeyCloakUtils.getResourceName(dto.getResourceId()));
            resource.setUri(dto.getUri());
            resource.setType(dto.getType());
            resource.setOwner(dto.getUserId());

            resource.setScopes(ResourceScope.getAllScopes());

            AuthorizationResource authorization = getAuthResource(keycloakInstance);
            Response response = authorization.resources().create(resource);

            if (!isSuccessStatus(response.getStatus())) {
                throw new KeyCloakActionFailedException(response.readEntity(String.class));
            }
        }
    }

    @Override
    public void removeResource(String id) {
        Keycloak keycloakInstance = keycloak.get();
        try (keycloakInstance) {
            AuthorizationResource authorization = getAuthResource(keycloakInstance);
            authorization.resources().resource(KeyCloakUtils.getResourceName(id)).remove();
        }
    }

    @Override
    public void addScopesToUser(AddScopesDTO dto) {
        Keycloak keycloakInstance = keycloak.get();
        try (keycloakInstance) {
            AuthorizationResource authorization = getAuthResource(keycloakInstance);
            ScopePermissionRepresentation scopePermission = getExistingScopePermission(keycloakInstance,
                    dto.getUserId(), dto.getResourceId());

            if (scopePermission == null) {
                scopePermission = new ScopePermissionRepresentation();
                scopePermission.setScopes(dto.getScopes());
                scopePermission.setName(KeyCloakUtils.getScopePermissionName(dto.getUserId(), dto.getResourceId()));
                scopePermission.setDescription("Grant specific scopes to user " + dto.getUserId());
                scopePermission.setOwner(dto.getUserId());
                scopePermission.setResources(Set.of(dto.getResourceId()));

                Response response = authorization.permissions().scope().create(scopePermission);

                if (!isSuccessStatus(response.getStatus())) {
                    throw new KeyCloakActionFailedException(response.readEntity(String.class));
                }
            } else {
                Set<String> existingScopes = new HashSet<>(scopePermission.getScopes());
                existingScopes.addAll(dto.getScopes());
                scopePermission.setScopes(existingScopes);
                authorization.permissions().scope().findById(scopePermission.getId()).update(scopePermission);
            }
        }
    }

    @Override
    public void removeScopesFromUser(RemoveScopesDTO dto) {
        Keycloak keycloakInstance = keycloak.get();
        try (keycloakInstance) {
            AuthorizationResource authorization = getAuthResource(keycloakInstance);
            ScopePermissionRepresentation scopePermission = getExistingScopePermission(keycloakInstance,
                    dto.getUserId(), dto.getResourceId());

            scopePermission.setScopes(scopePermission.getScopes().stream()
                    .filter(scope -> !dto.getScopes().contains(scope))
                    .collect(Collectors.toSet()));

            authorization.permissions().scope().findById(scopePermission.getId()).update(scopePermission);
        }
    }

    @Override
    public void removeAllScopesFromUser(RemoveScopesDTO dto) {
        Keycloak keycloakInstance = keycloak.get();
        try (keycloakInstance) {
            AuthorizationResource authorization = getAuthResource(keycloakInstance);
            ScopePermissionRepresentation scopePermission = getExistingScopePermission(keycloakInstance,
                    dto.getUserId(), dto.getResourceId());
            authorization.permissions().scope().findById(scopePermission.getId()).remove();
        }
    }

    @Override
    public ScopePermissionDTO getScopePermissionOfUser(String userId, String resourceId) {
        Keycloak keycloakInstance = keycloak.get();
        try (keycloakInstance) {
            return ScopePermissionConverter.convert(getExistingScopePermission(keycloakInstance,
                    userId, resourceId), new ScopePermissionDTO());
        }
    }

    private boolean isSuccessStatus(int status) {
        return HttpStatus.Series.valueOf(status).compareTo(HttpStatus.Series.SUCCESSFUL) == 0;
    }

    private AuthorizationResource getAuthResource(Keycloak keycloakInstance) {
        ClientsResource clients = keycloakInstance.realm(realm).clients();
        List<ClientRepresentation> clientList = clients.findByClientId(clientId);
        String clientUUID = clientList.get(0).getId();
        ClientResource clientResource = clients.get(clientUUID);
        return clientResource.authorization();
    }

    private ScopePermissionRepresentation getExistingScopePermission(Keycloak keycloakInstance,
                                                                     String userId, String resourceId) {
        AuthorizationResource authorization = getAuthResource(keycloakInstance);
        ScopePermissionsResource scopePermissionsResource = authorization.permissions().scope();
        ScopePermissionRepresentation permission = scopePermissionsResource
                .findByName(KeyCloakUtils.getScopePermissionName(userId, resourceId));
        String permissionUUID = permission.getId();
        ScopePermissionResource scopePermissionResource = scopePermissionsResource.findById(permissionUUID);
        permission.setScopes(scopePermissionResource.scopes().stream()
                .map(ScopeRepresentation::getName).collect(Collectors.toSet()));
        permission.setResources(scopePermissionResource.resources().stream()
                .map(ResourceRepresentation::getName).collect(Collectors.toSet()));
        return permission;
    }

}
