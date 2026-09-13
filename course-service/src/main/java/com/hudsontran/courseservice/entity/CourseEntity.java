package com.hudsontran.courseservice.entity;

import com.hudsontran.courseservice.enums.CourseLevel;
import com.hudsontran.courseservice.enums.CourseStatus;
import com.hudsontran.courseservice.enums.CourseVisibility;
import com.hudsontran.courseservice.enums.converter.CourseLevelConverter;
import com.hudsontran.courseservice.enums.converter.CourseStatusConverter;
import com.hudsontran.courseservice.enums.converter.CourseVisibilityConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "course")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "courseCategories")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 255)
    private String slug;

    @Column(name = "short_description", length = 1000)
    private String shortDescription;

    @Column(name = "description")
    private String description;

    @Convert(converter = CourseLevelConverter.class)
    @Column(name = "level", length = 30)
    private CourseLevel level;

    @Column(name = "language", length = 20)
    private String language;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    /**
     * Logical reference to auth-management-service.user.id.
     * No JPA relationship because User belongs to another bounded context.
     */
    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Convert(converter = CourseStatusConverter.class)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private CourseStatus status = CourseStatus.DRAFT;

    @Convert(converter = CourseVisibilityConverter.class)
    @Column(name = "visibility", nullable = false, length = 20)
    @Builder.Default
    private CourseVisibility visibility = CourseVisibility.PRIVATE;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(
            mappedBy = "course",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private Set<CourseCategoryEntity> courseCategories = new LinkedHashSet<>();

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}