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

    @Column
    private UUID classId;

    @Column
    private Integer maxSize;

    @Column
    private Integer currentSize;
}
