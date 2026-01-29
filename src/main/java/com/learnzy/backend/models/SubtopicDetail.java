package com.learnzy.backend.models;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonPropertyOrder({"id","title","content"})
@AllArgsConstructor
@NoArgsConstructor
public class SubtopicDetail {
    private String id;
    private String title;
    private String content;
}
