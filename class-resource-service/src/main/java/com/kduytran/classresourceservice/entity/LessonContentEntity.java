package com.kduytran.classresourceservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "lesson_content")
@Getter @Setter
public class LessonContentEntity {

    @Id
    @Column
    private UUID id;

    @Column
    private String content;

    @OneToOne(mappedBy = "lessonContent")
    private LessonEntity lesson;

}
