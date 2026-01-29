package com.learnzy.backend.models.search;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Match {
    private String type;
    private String topicTitle;
    private String subtopicId;
    private String subtopicTitle;
    private String snippet;
}
