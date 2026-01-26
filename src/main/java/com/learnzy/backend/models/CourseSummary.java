package com.learnzy.backend.models;

import lombok.Data;

@Data
public class CourseSummary {
    private String id;
    private String title;
    private String desciption;
    private int topicCount;
    private int subtopicCount;
}
