package com.kduytran.olpcommon.auth.annotation;

import jakarta.annotation.security.PermitAll;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PermitAll
public @interface PublicApi {
}
