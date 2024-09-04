package com.kduytran.memberservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "class_member")
@Getter
@Setter
public class ClassMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private UUID userId;

    @Column
    private UUID classId;

    @Column
    private ClassRole role;

    @Column
    private LocalDateTime joinedAt;

}
