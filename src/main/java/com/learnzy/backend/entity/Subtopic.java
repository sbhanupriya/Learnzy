package com.learnzy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Subtopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SUBTOPIC_ID")
    private Long id;
    @Column(name="SUBTOPIC_CODE")
    private String subtopicCode;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @ManyToOne
    @JoinColumn(name = "TOPIC_ID")
    private Topic topic;
}

