package com.learnzy.backend.entity;

import com.learnzy.backend.enums.EnrollmentStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ENROLLMENT_ID")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "COURSE_ID")
    private Course course;
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private Users user;
    @CreationTimestamp
    private OffsetDateTime createdDate;
    @UpdateTimestamp
    private OffsetDateTime updatedDate;
    private EnrollmentStatus status;
}
