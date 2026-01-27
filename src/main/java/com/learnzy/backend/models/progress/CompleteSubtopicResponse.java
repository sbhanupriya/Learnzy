package com.learnzy.backend.models.progress;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Builder
@Data
public class CompleteSubtopicResponse {
    private String subtopicId;
    private boolean completed;
    private OffsetDateTime completedAt;
}
