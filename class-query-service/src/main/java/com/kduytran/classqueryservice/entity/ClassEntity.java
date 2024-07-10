package com.kduytran.classqueryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "class")
@Getter @Setter
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
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    @Column
    private String status;

    @Column
    private String categoryId;

    @Column
    private String ownerType;

    @Column
    private String ownerId;

    @Column
    private Double averageRating;

    @Column
    private Long numberOfReviews;

}
