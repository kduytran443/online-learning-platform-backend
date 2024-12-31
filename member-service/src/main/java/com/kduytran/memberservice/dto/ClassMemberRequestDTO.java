package com.kduytran.memberservice.dto;

import com.kduytran.memberservice.entity.ClassRole;
import lombok.Data;

import java.util.UUID;

@Data
public class ClassMemberRequestDTO {
    private UUID classId;
    private UUID userId;
    private ClassRole role;
}
