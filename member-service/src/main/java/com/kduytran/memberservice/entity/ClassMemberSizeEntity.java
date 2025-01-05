package com.kduytran.memberservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "MB_CLASS_MEMBER_SIZE")
@Getter
@Setter
public class ClassMemberSizeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false, unique = true)
    private UUID classId;

    @Column(nullable = false)
    private int maxSize;

    @Column(nullable = false)
    private int currentSize;
}
