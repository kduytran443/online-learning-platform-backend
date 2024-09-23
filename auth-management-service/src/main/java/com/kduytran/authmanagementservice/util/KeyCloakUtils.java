package com.kduytran.authmanagementservice.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KeyCloakUtils {

    public static final String SCOPE_PERMISSION_NAME_FORMAT = "user_%s-resource_%s";
    public static final String RESOURCE_NAME_FORMAT = "resource_%s";

    public static String getScopePermissionName(String userId, String resourceId) {
        return String.format(SCOPE_PERMISSION_NAME_FORMAT, userId, resourceId);
    }

    public static String getResourceName(String id) {
        return String.format(RESOURCE_NAME_FORMAT, id);
    }

}
