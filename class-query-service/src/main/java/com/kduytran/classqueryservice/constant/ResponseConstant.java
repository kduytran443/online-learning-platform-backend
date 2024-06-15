package com.kduytran.classqueryservice.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseConstant {
    public static final String STATUS_200 = "200";
    public static final String MESSAGE_200 = "Request processed successfully";
    public static final String STATUS_201 = "201";
    public static final String MESSAGE_201 = "Class created successfully";
    public static final String STATUS_400 = "400";
    public static final String STATUS_417 = "417";
    public static final String MESSAGE_417_UPDATE = "Update operation failed. Please try again or contact Dev team";
    public static final String MESSAGE_417_DELETE = "Delete operation failed. Please try again or contact Dev team";
    public static final String STATUS_500 = "500";
    public static final String PASSWORD_MESSAGE_200 = "Password is correct";
    public static final String PASSWORD_MESSAGE_400 = "Password is incorrect";

}
