package com.kduytran.classresourceservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "assignment")
@Getter @Setter
public class AssignmentEntity extends BaseEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private Integer seq;

    @Column
    private String name;

    @Column
    private EntityStatus status;

    @Column
    private String description;

    @Column
    private Double coefficient;

    @Column
    private AssignmentType assignmentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private TopicEntity topic;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

}
