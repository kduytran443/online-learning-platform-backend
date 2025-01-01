package com.kduytran.memberservice.controller;

import com.kduytran.memberservice.dto.UserClassesResponseDTO;
import com.kduytran.memberservice.service.ClassMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/api/v1/class-members")
@RestController
@RequiredArgsConstructor
class ClassMemberController {

    private final ClassMemberService classMemberService;

    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    UserClassesResponseDTO getClassesByUserId(@PathVariable String userId) {
        return classMemberService.getClassesByUserId(UUID.fromString(userId));
    }
}
