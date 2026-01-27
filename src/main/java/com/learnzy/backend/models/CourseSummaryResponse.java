package com.learnzy.backend.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseSummaryResponse {
    private List<CourseSummary> courses;
}
