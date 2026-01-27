package com.learnzy.backend.models.progress;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class CompletedSubtopicDetail {
    private String subtopicId;
    private String subtopicTitle;
    private OffsetDateTime completedAt;
}
