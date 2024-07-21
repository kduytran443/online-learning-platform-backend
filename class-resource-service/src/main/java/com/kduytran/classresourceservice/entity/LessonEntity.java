package com.kduytran.classresourceservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "lesson")
@Getter @Setter
public class LessonEntity extends BaseEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private Integer seq;

    @Column
    private UUID ownerId;

    @Column
    private String name;

    @Column
    private EntityStatus status;

    @Column
    private String description;

    @OneToOne
    @JoinColumn(name = "lesson_content_id")
    private LessonContentEntity lessonContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private TopicEntity topic;

}
