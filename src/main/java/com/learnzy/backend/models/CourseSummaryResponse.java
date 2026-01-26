package com.learnzy.backend.models;

import lombok.Data;

import java.util.List;

@Data
public class CourseSummaryResponse {
    private List<CourseSummary> courses;
}
