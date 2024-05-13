package com.kduytran.classservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity(name = "class_description")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassDescriptionEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String content;

    @OneToOne
    @JoinColumn(name = "aClass")
    private ClassEntity aClass;
}
