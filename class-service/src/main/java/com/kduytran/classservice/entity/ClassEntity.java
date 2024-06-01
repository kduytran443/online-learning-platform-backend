package com.kduytran.classservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "class")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String image;

    @Column
    private String password;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    @Column
    private EntityStatus status;

    @Column
    private UUID categoryId;

    @Column
    private OwnerType ownerType;

    @Column(name = "class_owner_id")
    private UUID classOwnerId;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "aClass")
    private ClassDescriptionEntity classDescription;

}
