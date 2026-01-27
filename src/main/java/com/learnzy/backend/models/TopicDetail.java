package com.learnzy.backend.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonPropertyOrder({"id","title","subtopics"})
public class TopicDetail {
    private String id;
    private String title;
    private List<SubtopicDetail> subtopics;
}
