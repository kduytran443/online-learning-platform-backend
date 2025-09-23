package com.kduytran.authmanagementservice.projection;

import java.util.UUID;

public interface UserDetailsProjection {
    UUID getId();
    String getUsername();
    String getEmail();
    String getName();
    String getPicture();
}
