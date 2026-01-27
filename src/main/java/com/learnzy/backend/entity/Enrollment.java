package com.learnzy.backend.entity;

import com.learnzy.backend.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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
    @OneToMany(mappedBy = "enrollment")
    private List<Progress> progressList;
    @CreationTimestamp
    private OffsetDateTime createdDate;
    @UpdateTimestamp
    private OffsetDateTime updatedDate;
    private EnrollmentStatus status;

    public boolean isActive(){
        return this.status.equals(EnrollmentStatus.ENROLLED);
    }
}
