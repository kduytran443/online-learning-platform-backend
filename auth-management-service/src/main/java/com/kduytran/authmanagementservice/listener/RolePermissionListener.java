package com.kduytran.authmanagementservice.listener;

import com.kduytran.authmanagementservice.entity.PermissionEntity;
import com.kduytran.authmanagementservice.entity.RoleEntity;
import com.kduytran.authmanagementservice.properties.OlpSecurityProperties;
import com.kduytran.authmanagementservice.repository.PermissionRepository;
import com.kduytran.authmanagementservice.repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RolePermissionListener {

    private final OlpSecurityProperties securityProperties;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @EventListener({ApplicationReadyEvent.class, EnvironmentChangeEvent.class})
    public void initializeOrUpdateRolePermissions(ApplicationEvent event) {
        log.info("Syncing Roles and Permissions from application config...");

        for (OlpSecurityProperties.RoleDefinition roleDef : securityProperties.getRoles()) {
            RoleEntity role = roleRepository.findByName(roleDef.getName())
                    .orElseGet(() -> new RoleEntity(null, roleDef.getName(), new HashSet<>()));

            Set<PermissionEntity> permissionEntities = roleDef.getPermissions().stream()
                    .map(permissionName -> permissionRepository.findByName(permissionName)
                            .orElseGet(() -> permissionRepository.save(new PermissionEntity(null, permissionName))))
                    .collect(Collectors.toSet());
            role.setPermissions(permissionEntities);
            roleRepository.save(role);
            log.info("Role [{}] synced with permissions {}", role.getName(), role.getPermissions());
        }

        log.info("Sync complete.");
    }
}
