package com.hudsontran.courseservice.service;

import com.hudsontran.courseservice.dto.course.CourseResponse;
import com.hudsontran.courseservice.dto.course.CreateCourseRequest;
import com.hudsontran.courseservice.dto.course.ListCourseRequest;
import com.hudsontran.courseservice.dto.course.ListCourseResponse;
import com.hudsontran.courseservice.entity.CategoryEntity;
import com.hudsontran.courseservice.entity.CourseCategoryEntity;
import com.hudsontran.courseservice.entity.CourseEntity;
import com.hudsontran.courseservice.entity.ids.CourseCategoryId;
import com.hudsontran.courseservice.enums.CourseStatus;
import com.hudsontran.courseservice.enums.CourseVisibility;
import com.hudsontran.courseservice.extrafields.CourseExtraField;
import com.hudsontran.courseservice.extrafields.ExtraFieldsHandler;
import com.hudsontran.courseservice.mapper.CategoryMapper;
import com.hudsontran.courseservice.mapper.ClassMapper;
import com.hudsontran.courseservice.mapper.CourseCategoryMapper;
import com.hudsontran.courseservice.mapper.CourseMapper;
import com.hudsontran.courseservice.repository.CategoryRepository;
import com.hudsontran.courseservice.repository.CourseRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CourseMapper courseMapper;
    private final CourseCategoryMapper courseCategoryMapper;
    private final CategoryMapper categoryMapper;
    private final ClassMapper classMapper;
    ExtraFieldsHandler<CourseExtraField, CourseEntity, CourseResponse>
            extraFieldsHandler = new ExtraFieldsHandler<>(
                    CourseExtraField.class
            );


    public CourseService(
            CourseRepository courseRepository,
            CategoryRepository categoryRepository,
            CourseMapper courseMapper,
            CourseCategoryMapper courseCategoryMapper,
            CategoryMapper categoryMapper, ClassMapper classMapper
    ) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.courseMapper = courseMapper;
        this.courseCategoryMapper = courseCategoryMapper;
        this.categoryMapper = categoryMapper;
        this.classMapper = classMapper;

        extraFieldsHandler
                .setExtraValue(
                        CourseExtraField.CATEGORIES,
                        this::addCategories
                )
                .setExtraValue(
                        CourseExtraField.CLASSES,
                        this::addClasses
                );
    }

    public ListCourseResponse listMyCourses(UUID myId, ListCourseRequest request) {
        List<CourseEntity> myCourses = courseRepository.findByAuthorId(myId);
        var courseListDto = courseMapper.toResponse(myCourses);

        Set<CourseExtraField> extraFields = CourseExtraField.from(request.extraFields());
        extraFieldsHandler.executeList(extraFields, myCourses, courseListDto);
        return ListCourseResponse.builder()
                .courses(courseListDto)
                .build();
    }

    @Transactional
    public CourseResponse createCourse(
            CreateCourseRequest request,
            UUID authorId
    ) {
        String generatedSlug = "%s%s".formatted(request.slug(), RandomStringUtils.secure()
                .nextAlphanumeric(8).toLowerCase());
        validateSlug(generatedSlug);

        Set<UUID> categoryIds = request.categoryIds();

        List<CategoryEntity> categories = categoryRepository.findAllByIdIn(categoryIds);

        validateCategories(
                categoryIds,
                categories
        );

        CourseEntity course = courseMapper.toEntity(
                request,
                generatedSlug,
                authorId,
                CourseStatus.DRAFT
        );
        course.setStatus(CourseStatus.DRAFT);

        if (course.getVisibility() == null) {
            course.setVisibility(
                    CourseVisibility.PRIVATE
            );
        }

        for (CategoryEntity category : categories) {
            CourseCategoryEntity courseCategory = courseCategoryMapper.toEntity(
                            course,
                            category
                    );
            course.getCourseCategories().add(courseCategory);
        }

        for (CourseCategoryEntity relation : course.getCourseCategories()) {

            relation.setId(
                    CourseCategoryId.builder()
                            .courseId(course.getId())
                            .categoryId(
                                    relation.getCategory()
                                            .getId()
                            )
                            .build()
            );
        }

        CourseEntity saved = courseRepository.save(course);

        return toResponse(saved);
    }

    private void validateSlug(
            String slug
    ) {

        if (courseRepository.existsBySlug(slug)) {
            throw new IllegalArgumentException(
                    "Course slug already exists: " + slug
            );
        }
    }

    private void validateCategories(
            Set<UUID> requestedIds,
            List<CategoryEntity> categories
    ) {

        Set<UUID> foundIds =
                categories.stream()
                        .map(CategoryEntity::getId)
                        .collect(Collectors.toSet());

        Set<UUID> missingIds =
                requestedIds.stream()
                        .filter(id -> !foundIds.contains(id))
                        .collect(Collectors.toSet());

        if (!missingIds.isEmpty()) {
            throw new EntityNotFoundException(
                    "Categories not found: " + missingIds
            );
        }
    }

    private CourseResponse toResponse(
            CourseEntity course
    ) {

        Set<UUID> categoryIds =
                course.getCourseCategories()
                        .stream()
                        .map(
                                relation ->
                                        relation.getCategory().getId()
                        )
                        .collect(Collectors.toSet());

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .slug(course.getSlug())
                .shortDescription(
                        course.getShortDescription()
                )
                .description(
                        course.getDescription()
                )
                .level(course.getLevel())
                .language(course.getLanguage())
                .thumbnailUrl(
                        course.getThumbnailUrl()
                )
                .authorId(course.getAuthorId())
                .status(course.getStatus())
                .visibility(course.getVisibility())
                .publishedAt(course.getPublishedAt())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .categoryIds(categoryIds)
                .build();
    }

    private void addCategories(
            CourseEntity source,
            CourseResponse target
    ) {
        target.setCategories(
                source.getCourseCategories()
                        .stream()
                        .map(CourseCategoryEntity::getCategory)
                        .map(categoryMapper::toResponse)
                        .toList()
        );
    }

    private void addClasses(
            CourseEntity course,
            CourseResponse response
    ) {
        response.setClasses(
                course.getClasses()
                        .stream()
                        .map(classMapper::toResponse)
                        .toList()
        );
    }
}
