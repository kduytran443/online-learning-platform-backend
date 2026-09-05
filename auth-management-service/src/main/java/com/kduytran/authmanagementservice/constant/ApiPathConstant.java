package com.kduytran.authmanagementservice.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiPathConstant {
    // V1 Auth APIs
    public static final String V1_AUTH_REGISTRATION = "/api/v1/auth/registration";
    public static final String V1_AUTH_VERIFICATION = "/api/v1/auth/verification";
    public static final String V1_AUTH_REFRESH_VERIFICATION = "/api/v1/auth/refresh-verification";

    // V1 Login
    public static final String V1_LOGIN_SUCCESS = "/api/v1/login-success";
}
