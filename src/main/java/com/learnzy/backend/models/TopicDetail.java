package com.learnzy.backend.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@JsonPropertyOrder({"id","title","subtopics"})
@AllArgsConstructor
@NoArgsConstructor
public class TopicDetail {
    private String id;
    private String title;
    private List<SubtopicDetail> subtopics;
}
