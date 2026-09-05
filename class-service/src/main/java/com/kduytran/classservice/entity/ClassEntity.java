package com.kduytran.classservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity(name = "olp_class")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassEntity extends BaseEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String thumbnailImageKey;

    @Column
    private String bannerImageKey;

    @Column
    @Enumerated(EnumType.STRING)
    private Accessibility accessibility;

    @Column
    private UUID categoryId;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "archived_at")
    private Instant archivedAt;

    @OneToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "class_description_id")
    private ClassDescriptionEntity classDescription;
}
