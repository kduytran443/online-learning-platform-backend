package com.kduytran.userservice.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseConstant {
    public static final String STATUS_200 = "200";
    public static final String MESSAGE_200 = "Request processed successfully";
    public static final String STATUS_201 = "201";
    public static final String MESSAGE_201 = "User created successfully";
    public static final String REFRESH_VERIFICATION_MESSAGE_201 = "Refresh verification created successfully";
    public static final String STATUS_417 = "201";
    public static final String MESSAGE_417_UPDATE = "Update operation failed. Please try again or contact Dev team";
    public static final String MESSAGE_417_DELETE = "Delete operation failed. Please try again or contact Dev team";
    public static final String STATUS_500 = "500";

}
