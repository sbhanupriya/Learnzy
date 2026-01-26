package com.learnzy.backend.models;

import lombok.Data;

import java.util.List;

@Data
public class CourseDetail {
    private String id;
    private String title;
    private String description;
    private List<TopicDetail> topics;
}
