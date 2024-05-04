package com.kduytran.categoryservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity(name = "category")
@Getter @Setter
public class CategoryEntity extends BaseEntity {

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

    @Column(name = "category_status")
    private EntityStatus status;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private List<CategoryEntity> subCategories;

    @ManyToOne
    @JoinColumn(columnDefinition = "parent_category_id")
    private CategoryEntity parentCategory;

    /**
     * Counts the number of parent categories for a given category entity.
     *
     * @return The number of parent categories leading up to the root category.
     */
    public int getParentCount() {
        return getAllParents().size();
    }

    /**
     * Retrieves a list of all parent categories for the current category, ordered from
     * the immediate parent to the root parent. This order follows the natural hierarchy
     * from the current category back to the root.
     *
     * @return a list of parent {@link CategoryEntity} objects, ordered from the closest parent
     *         to the farthest. If there are no parents, the list will be empty.
     */
    private List<CategoryEntity> getAllParents() {
        List<CategoryEntity> allParents = new ArrayList<>();
        CategoryEntity parent = getParentCategory();
        while (parent != null) {
            allParents.add(parent);
            parent = parent.getParentCategory();
        }
        return allParents;
    }

    /**
     * Retrieves a list of all parent categories for the current category, ordered from
     * the root parent to the immediate parent. This order represents the parent hierarchy
     * in reverse, providing the lineage from the root down to the current category.
     *
     * @return a list of parent {@link CategoryEntity} objects, ordered from the farthest
     *         parent to the closest. If there are no parents, the list will be empty.
     */
    private List<CategoryEntity> getAllParentsInReverse() {
        List<CategoryEntity> allParents = getAllParents();
        Collections.reverse(allParents);
        return allParents;
    }

}
