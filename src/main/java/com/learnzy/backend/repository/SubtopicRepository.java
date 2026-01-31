package com.learnzy.backend.repository;

import com.learnzy.backend.entity.Course;
import com.learnzy.backend.entity.Subtopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubtopicRepository extends JpaRepository<Subtopic, Long> {
    Optional<Subtopic> findBySubtopicCodeIgnoreCase(String code);
}
