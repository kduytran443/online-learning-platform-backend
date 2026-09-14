package com.hudsontran.courseservice.extrafields;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum CourseExtraField {
    CLASSES,
    CATEGORIES;

    public static Set<CourseExtraField> from(
            List<String> values
    ) {
        if (values == null || values.isEmpty()) {
            return Set.of();
        }

        return values.stream()
                .map(String::trim)
                .map(String::toUpperCase)
                .map(CourseExtraField::tryParse)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
    }

    private static Optional<CourseExtraField> tryParse(
            String value
    ) {
        try {
            return Optional.of(
                    valueOf(value)
            );
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
