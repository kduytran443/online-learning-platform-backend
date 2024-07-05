package com.kduytran.classqueryservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity(name = "category")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @Column
    private String code;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private List<CategoryEntity> subCategories;

    @ManyToOne
    @JoinColumn(columnDefinition = "parent_category_id")
    private CategoryEntity parentCategory;

}
