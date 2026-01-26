package com.learnzy.backend.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Builder
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TOPIC_ID")
    private Long id;
    @Column(name="TOPIC_CODE")
    private String topicCode;
    private String title;
    @ManyToOne
    @JoinColumn(name="COURSE_ID")
    private Course course;
    @OneToMany(mappedBy = "topic")
    private List<Subtopic> subtopicList;
}
