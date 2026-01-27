package com.learnzy.backend.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id", "title", "description", "topicCount", "subtopicCount"})
public class CourseSummary {
    private String id;
    private String title;
    private String description;
    private int topicCount;
    private Long subtopicCount;
}
