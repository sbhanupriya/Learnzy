package com.learnzy.backend.repository;

import com.learnzy.backend.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic,Long> {

}
