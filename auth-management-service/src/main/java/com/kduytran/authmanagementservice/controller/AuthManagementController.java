package com.kduytran.authmanagementservice.controller;

import com.kduytran.authmanagementservice.constant.ResponseConstant;
import com.kduytran.authmanagementservice.dto.*;
import com.kduytran.authmanagementservice.service.IAuthManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth-management")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthManagementController {
    private final IAuthManagementService authManagementService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserByUsername(@RequestParam String username) {
        return authManagementService.getUserByUsername(username);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO registerUser(@RequestBody @Valid UserRequestDTO dto) {
        log.info("Registering user with request: userRequestDTO={}", dto);
        authManagementService.registerUser(dto);
        return ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO updateUser(@PathVariable String id, @RequestBody @Valid UserRequestDTO dto) {
        log.info("Updating user with request: id={}, userRequestDTO={}", id, dto);
        authManagementService.updateUser(id, dto);
        return ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO removeUser(@PathVariable String id) {
        log.info("Removing user with request: id={}", id);
        authManagementService.removeUser(id);
        return ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200);
    }

    @PostMapping("/resources")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO createResource(@RequestBody @Valid ResourceRequestDTO dto) {
        log.info("Creating resource with request: resourceRequestDTO={}", dto);
        authManagementService.createResource(dto);
        return ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201);
    }

    @DeleteMapping("/resources/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO removeResource(@PathVariable String id) {
        log.info("Removing resource with request: id={}", id);
        authManagementService.removeResource(id);
        return ResponseDTO.of(ResponseConstant.STATUS_200, ResponseConstant.MESSAGE_200);
    }

    @PostMapping("/resource-scopes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO addScopesToUser(@RequestBody @Valid AddScopesDTO dto) {
        log.info("Adding resource scopes to user with request: addScopesDTO={}", dto);
        authManagementService.addScopesToUser(dto);
        return ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201);
    }

    @DeleteMapping("/resource-scopes")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO removeScopesFromUser(@RequestBody @Valid RemoveScopesDTO dto) {
        log.info("Removing resource scopes from user with request: removeScopesDTO={}", dto);
        authManagementService.removeScopesFromUser(dto);
        return ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201);
    }

    @DeleteMapping("/resource-scopes/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDTO removeAllScopesFromUser(@RequestBody @Valid RemoveScopesDTO dto) {
        log.info("Removing all resource scopes from user with request: removeScopesDTO={}", dto);
        authManagementService.removeAllScopesFromUser(dto);
        return ResponseDTO.of(ResponseConstant.STATUS_201, ResponseConstant.MESSAGE_201);
    }

    @GetMapping("/resource-scopes")
    @ResponseStatus(HttpStatus.OK)
    public ScopePermissionDTO getScopePermissionOfUser(@RequestParam String userId, @RequestParam String resourceId) {
        log.info("Getting resource scopes of user with request: id={}, resourceId={}", userId, resourceId);
        return authManagementService.getScopePermissionOfUser(userId, resourceId);
    }

}
