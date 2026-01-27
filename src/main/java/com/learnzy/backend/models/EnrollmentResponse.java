package com.learnzy.backend.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Builder
@Data
@JsonPropertyOrder({"enrollmentId", "courseId", "courseTitle","enrolledAt"})
public class EnrollmentResponse {
    private Long enrollmentId;
    private String courseId;
    private String courseTitle;
    private OffsetDateTime enrolledAt;
}
