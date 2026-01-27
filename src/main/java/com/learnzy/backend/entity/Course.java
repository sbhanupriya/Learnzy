package com.learnzy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COURSE_ID")
    private Long id;
    @Column(name="COURSE_CODE", unique = true)
    private String courseCode;
    private String title;
    private String description;
    @OneToMany(mappedBy = "course")
    private List<Topic> topicList;
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollmentList;
}
