package com.learnzy.backend.models;

import lombok.Data;

import java.util.List;

@Data
public class CourseDetailResponse {
    private List<CourseDetail> courses;
}
