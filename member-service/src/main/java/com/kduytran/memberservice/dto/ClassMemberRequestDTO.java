package com.kduytran.memberservice.dto;

import com.kduytran.memberservice.entity.ClassRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ClassMemberRequestDTO {
    private UUID classId;
    private UUID userId;
    private ClassRole role;
}
