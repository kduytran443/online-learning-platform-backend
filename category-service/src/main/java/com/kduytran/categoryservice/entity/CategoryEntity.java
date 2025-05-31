package com.kduytran.categoryservice.entity;

import com.kduytran.olpcommon.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "category")
public class CategoryEntity extends BaseEntity {

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String code;

    @Column(nullable = false)
    private boolean deleted;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CategoryEntity> subCategories;

    @ManyToOne
    @JoinColumn(columnDefinition = "parent_category_id")
    private CategoryEntity parentCategory;

    public List<CategoryEntity> getAllParents() {
        List<CategoryEntity> allParents = new ArrayList<>();
        CategoryEntity parent = getParentCategory();
        while (parent != null) {
            allParents.add(parent);
            parent = parent.getParentCategory();
        }
        return allParents;
    }
}
