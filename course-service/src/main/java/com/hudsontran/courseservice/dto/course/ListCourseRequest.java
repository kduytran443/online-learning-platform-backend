package com.hudsontran.courseservice.dto.course;

import lombok.Builder;

import java.util.List;

@Builder
public record ListCourseRequest(
        List<String> extraFields
) {
}
