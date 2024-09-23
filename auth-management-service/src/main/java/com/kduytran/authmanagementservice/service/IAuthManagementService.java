package com.kduytran.authmanagementservice.service;

import com.kduytran.authmanagementservice.dto.*;

public interface IAuthManagementService {
    void removeUser(String userId);
    void updateUser(String id, UserRequestDTO dto);
    void registerUser(UserRequestDTO dto);
    UserDTO getUserByUsername(String username);
    void createResource(ResourceRequestDTO dto);
    void removeResource(String id);
    void addScopesToUser(AddScopesDTO dto);
    void removeScopesFromUser(RemoveScopesDTO dto);
    void removeAllScopesFromUser(RemoveScopesDTO dto);
    ScopePermissionDTO getScopePermissionOfUser(String userId, String resourceId);
}
