package com.kduytran.olpcommon.auth.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(
        "hasRole('{role}') or hasAuthority('SCOPE_{scope}')"
)
public @interface HasRoleOrScope {
    String role();
    String scope();
}
