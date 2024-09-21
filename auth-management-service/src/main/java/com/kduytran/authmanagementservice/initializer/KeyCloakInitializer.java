package com.kduytran.authmanagementservice.initializer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@Slf4j
public class KeyCloakInitializer {
    private final Supplier<Keycloak> keycloak;
    private final String realm;

    public KeyCloakInitializer(Supplier<Keycloak> keycloak, @Value("${keycloak.realm}") String realm) {
        this.keycloak = keycloak;
        this.realm = realm;
    }

    @PostConstruct
    public void init() {
        Keycloak keycloakInstance = keycloak.get();
        RealmResource realmResource = keycloakInstance.realm(realm);
        RolesResource rolesResource = realmResource.roles();

        createRoleIfNotExists(rolesResource, "OWNER");
        createRoleIfNotExists(rolesResource, "STUDENT");
        createRoleIfNotExists(rolesResource, "ADMIN");
    }

    private void createRoleIfNotExists(RolesResource rolesResource, String roleName) {
        if (rolesResource.get(roleName) == null) {
            RoleRepresentation role = new RoleRepresentation();
            role.setName(roleName);
            rolesResource.create(role);
            log.info("Created role: " + roleName);
        } else {
            log.info("Role already exists: " + roleName);
        }
    }

}
