package com.learnzy.backend.models.progress;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProgressResponse {
    private Long enrollmentId;
    private String courseId;
    private String courseTitle;
    private Long totalSubtopics;
    private Long completedSubtopics;
    private BigDecimal completionPercentage;
    private List<CompletedSubtopicDetail> completedItems;
}
