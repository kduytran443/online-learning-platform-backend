package com.hudsontran.courseservice.entity;

import com.hudsontran.courseservice.enums.AccessPolicy;
import com.hudsontran.courseservice.enums.ClassStatus;
import com.hudsontran.courseservice.enums.ClassVisibility;
import com.hudsontran.courseservice.enums.EnrollmentPolicy;
import com.hudsontran.courseservice.enums.converter.AccessPolicyConverter;
import com.hudsontran.courseservice.enums.converter.ClassStatusConverter;
import com.hudsontran.courseservice.enums.converter.ClassVisibilityConverter;
import com.hudsontran.courseservice.enums.converter.EnrollmentPolicyConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "class")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "course")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ClassEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    /**
     * Course that this class is offering.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "course_id",
            nullable = false
    )
    private CourseEntity course;

    /**
     * Logical reference to auth-management-service.user.id.
     */
    @Column(name = "instructor_id", nullable = false)
    private UUID instructorId;

    @Column(name = "instructor_name", length = 255)
    private String instructorName;

    @Column(name = "price", precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name = "max_students")
    private Integer maxStudents;

    /**
     * Current number of students.
     *
     * This can either be maintained by this service as a denormalized
     * counter or calculated from enrollment-service depending on
     * the final architecture.
     */
    @Column(name = "current_students", nullable = false)
    @Builder.Default
    private Integer currentStudents = 0;

    @Convert(converter = ClassStatusConverter.class)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private ClassStatus status = ClassStatus.DRAFT;

    @Convert(converter = ClassVisibilityConverter.class)
    @Column(name = "visibility", nullable = false, length = 20)
    @Builder.Default
    private ClassVisibility visibility = ClassVisibility.PRIVATE;

    @Convert(converter = EnrollmentPolicyConverter.class)
    @Column(name = "enrollment_policy", nullable = false, length = 30)
    @Builder.Default
    private EnrollmentPolicy enrollmentPolicy = EnrollmentPolicy.OPEN;

    @Convert(converter = AccessPolicyConverter.class)
    @Column(name = "access_policy", nullable = false, length = 30)
    @Builder.Default
    private AccessPolicy accessPolicy = AccessPolicy.FULL;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;

        if (price == null) {
            price = BigDecimal.ZERO;
        }

        if (currentStudents == null) {
            currentStudents = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
