package com.learnzy.backend.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonPropertyOrder({"id","title","content"})
public class SubtopicDetail {
    private String id;
    private String title;
    private String content;
}
