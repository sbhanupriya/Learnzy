package com.learnzy.backend.models.search;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class SearchResponse {
    private String query;
    List<SearchResult> results;
}
