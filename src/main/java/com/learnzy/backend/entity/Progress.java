package com.learnzy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="PROGRESS_ID")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "ENROLLMENT_ID")
    private Enrollment enrollment;
    @ManyToOne
    @JoinColumn(name="SUBTOPIC_ID")
    private Subtopic subtopic;
    @CreationTimestamp
    private OffsetDateTime createdOn;
}
