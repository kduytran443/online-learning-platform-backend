package com.kduytran.authmanagementservice.service;

import com.kduytran.authmanagementservice.entity.RoleEntity;

import java.util.Set;

public interface RoleService {

    Set<RoleEntity> getRoleByName(String roleName);
}
