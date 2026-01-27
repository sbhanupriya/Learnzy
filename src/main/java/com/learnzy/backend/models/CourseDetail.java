package com.learnzy.backend.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonPropertyOrder({"id","title","description","topics"})
public class CourseDetail {
    private String id;
    private String title;
    private String description;
    private List<TopicDetail> topics;
}
