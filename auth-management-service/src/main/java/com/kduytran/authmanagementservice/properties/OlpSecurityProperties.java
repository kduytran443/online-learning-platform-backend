package com.kduytran.authmanagementservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "olp.security")
public class OlpSecurityProperties {
    private List<RoleDefinition> roles;

    @Data
    public static class RoleDefinition {
        private String name;
        private List<String> permissions;
    }
}
