package com.kduytran.authmanagementservice.service.impl;

import com.kduytran.authmanagementservice.dto.UserResponseDto;
import com.kduytran.authmanagementservice.exception.UserNotFoundException;
import com.kduytran.authmanagementservice.properties.KeyCloakProps;
import com.kduytran.authmanagementservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final KeyCloakProps keyCloakProps;
    private final Keycloak keycloak;

    @Cacheable(
            cacheNames = "users",
            key = "#id + ':' + #attachRoles"
    )
    @Override
    public UserResponseDto getById(String id, boolean attachRoles) {
        var user = keycloak.realm(keyCloakProps.getRealm())
                .users()
                .get(id)
                .toRepresentation();
        return getUser(user, attachRoles);
    }

    @Cacheable(
            cacheNames = "users",
            key = "#username + ':' + #attachRoles"
    )
    @Override
    public UserResponseDto getByUsername(String username, boolean attachRoles) {
        var users = keycloak.realm(keyCloakProps.getRealm())
                .users()
                .searchByUsername(username, true);

        if (users.isEmpty()) {
            throw new UserNotFoundException("User not found: " + username);
        }
        UserRepresentation userRepresentation = users.get(0);
        return getUser(userRepresentation, attachRoles);
    }

    private UserResponseDto getUser(UserRepresentation userRepresentation, boolean attachRoles) {
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .emailVerified(userRepresentation.isEmailVerified())
                .familyName(userRepresentation.getLastName())
                .givenName(userRepresentation.getFirstName())
                .name(buildName(
                        userRepresentation.getFirstName(),
                        userRepresentation.getLastName()
                ))
                .email(userRepresentation.getEmail())
                .build();
        if (attachRoles) {
            userResponseDto.setRoles(getRolesByUserId(userRepresentation.getId()));
        }
        return userResponseDto;
    }

    private String buildName(String firstName, String lastName) {
        return "%s %s".formatted(firstName, lastName);
    }

    @Override
    public List<UserResponseDto> getAllByUsername(List<String> usernames) {
        return List.of();
    }

    public List<String> getRolesByUserId(String userId) {
        return keycloak
                .realm(keyCloakProps.getRealm())
                .users()
                .get(userId)
                .roles()
                .realmLevel()
                .listEffective()
                .stream()
                .map(RoleRepresentation::getName)
                .toList();
    }
}
