package com.kduytran.categoryservice.entity;

import com.kduytran.categoryservice.entity.attrconverter.EntityStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity(name = "category")
@Getter @Setter
public class CategoryEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String code;

    @Column(name = "status")
    private EntityStatus status;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private List<CategoryEntity> subCategories;

    @ManyToOne
    @JoinColumn(columnDefinition = "parent_category_id")
    private CategoryEntity parentCategory;

}
