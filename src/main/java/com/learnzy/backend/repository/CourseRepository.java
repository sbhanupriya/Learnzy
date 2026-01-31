package com.learnzy.backend.repository;

import com.learnzy.backend.entity.Course;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    Optional<Course> findByCourseCodeIgnoreCase(String code);
}
