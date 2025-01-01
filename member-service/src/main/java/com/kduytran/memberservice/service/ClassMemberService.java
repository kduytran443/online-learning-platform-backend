package com.kduytran.memberservice.service;

import com.kduytran.memberservice.dto.ClassMemberRequestDTO;
import com.kduytran.memberservice.dto.UserClassesResponseDTO;

import java.util.UUID;

public interface ClassMemberService {

    UUID joinClass(ClassMemberRequestDTO dto);

    UserClassesResponseDTO getClassesByUserId(UUID userId);
}
