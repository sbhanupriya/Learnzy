package com.learnzy.backend.models;

import lombok.Data;

import java.util.List;

@Data
public class TopicDetail {
    private String id;
    private String title;
    private List<SubtopicDetail> subtopics;
}
