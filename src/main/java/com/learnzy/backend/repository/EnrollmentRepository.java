package com.learnzy.backend.repository;

import com.learnzy.backend.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    public Enrollment findByUserIdAndCourseId(Long userId, Long courseId);
    public List<Enrollment> findAllByUserId(Long userId);
}
