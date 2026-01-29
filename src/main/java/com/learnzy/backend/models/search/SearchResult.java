package com.learnzy.backend.models.search;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SearchResult {
    private String courseId;
    private String courseTitle;
    private List<Match> matches;
}
