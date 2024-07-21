package com.kduytran.classresourceservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "topic")
@Getter @Setter
public class TopicEntity extends BaseEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private Integer seq;

    @Column
    private EntityStatus status;

    @Column
    private UUID ownerId;

    @Column
    private UUID classId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic")
    private List<LessonEntity> lessons;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "topic")
    private List<AssignmentEntity> assignments;

}
