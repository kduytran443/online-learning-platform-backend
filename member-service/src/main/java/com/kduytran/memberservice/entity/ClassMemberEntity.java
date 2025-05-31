package com.kduytran.memberservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "MB_CLASS_MEMBER")
@Getter
@Setter
public class ClassMemberEntity {

    @Id
    private UUID id;

    @Column
    private UUID userId;

    @Column
    private UUID classId;

    @Column
    @Enumerated(EnumType.STRING)
    private ClassRole role;

    @Column
    private LocalDateTime joinedAt;

    @PrePersist
    private void prePersis() {
        if (getId() == null) {
            setId(UUID.randomUUID());
        }
    }
}
