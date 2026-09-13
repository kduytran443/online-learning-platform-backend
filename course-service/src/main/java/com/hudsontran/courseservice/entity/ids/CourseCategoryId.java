package com.hudsontran.courseservice.entity.ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CourseCategoryId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "course_id")
    private UUID courseId;

    @Column(name = "category_id")
    private UUID categoryId;
}
