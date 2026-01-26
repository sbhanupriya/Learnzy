package com.learnzy.backend.utils;


import com.learnzy.backend.config.Config;
import com.learnzy.backend.entity.Course;
import com.learnzy.backend.entity.Subtopic;
import com.learnzy.backend.entity.Topic;
import com.learnzy.backend.models.CourseDetail;
import com.learnzy.backend.models.CourseDetailResponse;
import com.learnzy.backend.repository.CourseRepository;
import com.learnzy.backend.repository.SubtopicRepository;
import com.learnzy.backend.repository.TopicRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Log4j2
public class LoadDataService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SubtopicRepository subtopicRepository;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private Config config;

    @PostConstruct
    public void loadData() throws IOException {
        try {
            if (insertSeedData()) {
                log.info("Seed Data Insert Started");

                ClassPathResource resource = new ClassPathResource(config.getFilePath());
                CourseDetailResponse response = objectMapper.readValue(resource.getInputStream(), CourseDetailResponse.class);

                List<Course> courseList = new ArrayList<>();
                List<Topic> topicList = new ArrayList<>();
                List<Subtopic> subtopicList = new ArrayList<>();


                courseList.addAll(response.getCourses().stream().map(
                        courseDetail -> {
                            Course currCourse = Course.builder()
                                    .courseCode(courseDetail.getId())
                                    .title(courseDetail.getTitle())
                                    .description(courseDetail.getDescription())
                                    .build();

                            topicList.addAll(courseDetail.getTopics().stream().map(
                                    topicDetail -> {
                                        Topic currTopic = Topic.builder()
                                                .course(currCourse)
                                                .title(topicDetail.getTitle())
                                                .topicCode(topicDetail.getId())
                                                .build();


                                        subtopicList.addAll(topicDetail.getSubtopics().stream().map(
                                                subtopicDetail -> {
                                                    Subtopic subtopicCurr = Subtopic.builder()
                                                            .topic(currTopic)
                                                            .subtopicCode(subtopicDetail.getId())
                                                            .title(subtopicDetail.getTitle())
                                                            .content(subtopicDetail.getContent())
                                                            .build();
                                                    return subtopicCurr;
                                                }).collect(Collectors.toList()));

                                        return currTopic;
                                    }).collect(Collectors.toList()));

                            return currCourse;
                        }).collect(Collectors.toList()));


                courseRepository.saveAll(courseList);
                topicRepository.saveAll(topicList);
                subtopicRepository.saveAll(subtopicList);

                log.info("Seed data inserted");
            }
        } catch(Exception  exception){
            log.error("Error while saving seed data {} ", exception.getMessage());
        }
    }

    private boolean insertSeedData(){
        return courseRepository.findAll().isEmpty();
    }
}
