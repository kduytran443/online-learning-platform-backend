package com.hudsontran.courseservice.controller;

import com.hudsontran.courseservice.dto.course.CourseResponse;
import com.hudsontran.courseservice.dto.course.CreateCourseRequest;
import com.hudsontran.courseservice.dto.course.ListCourseRequest;
import com.hudsontran.courseservice.dto.course.ListCourseResponse;
import com.hudsontran.courseservice.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/courses")
    public CourseResponse createCourse(
            @Valid @RequestBody CreateCourseRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID authorId = UUID.fromString(jwt.getId());
        return courseService.createCourse(
                request,
                authorId
        );
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/api/v1/courses/me")
    public ListCourseResponse getMyCourses(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody ListCourseRequest request
    ) {
        UUID myId = UUID.fromString(jwt.getId());
        return courseService.listMyCourses(myId, request);
    }
}
