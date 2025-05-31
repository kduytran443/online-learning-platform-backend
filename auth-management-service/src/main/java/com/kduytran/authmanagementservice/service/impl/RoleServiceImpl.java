package com.kduytran.authmanagementservice.service.impl;

import com.kduytran.authmanagementservice.entity.RoleEntity;
import com.kduytran.authmanagementservice.repository.RoleRepository;
import com.kduytran.authmanagementservice.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Transactional
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Set<RoleEntity> getRoleByName(String roleName) {
        return Set.of();
    }
}
