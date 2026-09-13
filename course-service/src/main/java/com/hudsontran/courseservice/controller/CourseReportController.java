package com.hudsontran.courseservice.controller;

import com.hudsontran.courseservice.pdf.CoursePdfGenerator;
import com.hudsontran.courseservice.seed.ReportData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CourseReportController {

    private final CoursePdfGenerator coursePdfGenerator;

    @GetMapping(
            value = "/api/v1/courses/{courseId}/report/pdf",
            produces = "application/pdf"
    )
    public ResponseEntity<byte[]> generateCourseReport(
            @PathVariable UUID courseId
    ) {
        byte[] pdf = coursePdfGenerator.generate(ReportData.reportData);

        String filename =
                "course-" + courseId + "-report.pdf";

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.APPLICATION_PDF
        );

        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(filename)
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdf);
    }
}
