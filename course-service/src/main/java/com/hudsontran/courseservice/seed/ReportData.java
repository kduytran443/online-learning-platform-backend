package com.hudsontran.courseservice.seed;

import com.hudsontran.courseservice.dto.ClassReportData;
import com.hudsontran.courseservice.dto.CourseReportData;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ReportData {
    public static final CourseReportData reportData = CourseReportData.builder()
            .id(UUID.fromString("11111111-1111-1111-1111-111111111111"))
            .title("Java Spring Boot from Zero to Production")
            .slug("java-spring-boot-from-zero-to-production")
            .shortDescription(
                    "Learn Java Spring Boot from fundamentals to production-ready applications."
            )
            .description("""
                Complete Spring Boot course covering:
                Java fundamentals, Spring Core, Spring Boot, Spring Security,
                Apache Kafka, Microservices and production deployment.
                """)
            .level("INTERMEDIATE")
            .language("vi")
            .authorId(
                    UUID.fromString("22222222-2222-2222-2222-222222222222")
            )
            .status("PUBLISHED")
            .visibility("PUBLIC")
            .publishedAt(
                    Instant.parse("2026-09-10T08:00:00Z")
            )
            .createdAt(
                    Instant.parse("2026-09-01T08:00:00Z")
            )
            .updatedAt(
                    Instant.parse("2026-09-14T08:00:00Z")
            )
            .classes(List.of(

                    ClassReportData.builder()
                            .id(UUID.fromString(
                                    "31111111-1111-1111-1111-111111111111"
                            ))
                            .name("Spring Boot VIP K01")
                            .instructorId(UUID.fromString(
                                    "33333333-3333-3333-3333-333333333333"
                            ))
                            .instructorName("Nguyen Van A")
                            .price(new BigDecimal("2000000"))
                            .maxStudents(20)
                            .currentStudents(18)
                            .status("OPEN")
                            .visibility("PUBLIC")
                            .enrollmentPolicy("OPEN")
                            .accessPolicy("FULL")
                            .createdAt(
                                    Instant.parse("2026-09-05T08:00:00Z")
                            )
                            .updatedAt(
                                    Instant.parse("2026-09-14T08:00:00Z")
                            )
                            .build(),

                    ClassReportData.builder()
                            .id(UUID.fromString(
                                    "32222222-2222-2222-2222-222222222222"
                            ))
                            .name("Spring Boot Standard K02")
                            .instructorId(UUID.fromString(
                                    "44444444-4444-4444-4444-444444444444"
                            ))
                            .instructorName("Tran Thi B")
                            .price(new BigDecimal("500000"))
                            .maxStudents(100)
                            .currentStudents(76)
                            .status("OPEN")
                            .visibility("PUBLIC")
                            .enrollmentPolicy("OPEN")
                            .accessPolicy("STANDARD")
                            .createdAt(
                                    Instant.parse("2026-09-06T08:00:00Z")
                            )
                            .updatedAt(
                                    Instant.parse("2026-09-14T08:00:00Z")
                            )
                            .build(),

                    ClassReportData.builder()
                            .id(UUID.fromString(
                                    "33333333-3333-3333-3333-333333333333"
                            ))
                            .name("Spring Boot Free")
                            .instructorId(UUID.fromString(
                                    "55555555-5555-5555-5555-555555555555"
                            ))
                            .instructorName("Le Van C")
                            .price(BigDecimal.ZERO)
                            .maxStudents(500)
                            .currentStudents(421)
                            .status("OPEN")
                            .visibility("PUBLIC")
                            .enrollmentPolicy("OPEN")
                            .accessPolicy("FREE")
                            .createdAt(
                                    Instant.parse("2026-09-07T08:00:00Z")
                            )
                            .updatedAt(
                                    Instant.parse("2026-09-14T08:00:00Z")
                            )
                            .build()

            ))
            .build();
}
