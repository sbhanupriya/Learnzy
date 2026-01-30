package com.learnzy.backend.utils;

import com.learnzy.backend.config.Config;
import com.learnzy.backend.entity.Course;
import com.learnzy.backend.entity.LearningSearchDocument;
import com.learnzy.backend.entity.Subtopic;
import com.learnzy.backend.entity.Topic;
import com.learnzy.backend.enums.ContentType;
import com.learnzy.backend.models.CourseDetailResponse;
import com.learnzy.backend.repository.CourseRepository;
import com.learnzy.backend.repository.SubtopicRepository;
import com.learnzy.backend.repository.TopicRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
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

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @PostConstruct
    public void loadData() throws IOException {
        try {

            ClassPathResource resource = new ClassPathResource(config.getFilePath());
            CourseDetailResponse response = objectMapper.readValue(resource.getInputStream(), CourseDetailResponse.class);


            if (insertSeedDataInDb()) {
                log.info("Seed Data Insert Started");

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

            if(insertInElasticSearch()){
                log.info("Start Insert Data for Elastic Search");

                List<LearningSearchDocument> learningSearchCourses = new ArrayList<>();
                List<LearningSearchDocument> learningSearchTopics = new ArrayList<>();
                List<LearningSearchDocument> learningSearchSubtopics = new ArrayList<>();


                learningSearchCourses.addAll(response.getCourses().stream().map(
                        courseDetail -> {
                            LearningSearchDocument currCourse = LearningSearchDocument.builder()
                                    .title(courseDetail.getTitle())
                                    .path(courseDetail.getId())
                                    .contentType(ContentType.COURSE)
                                    .fieldId(courseDetail.getId())
                                    .content(courseDetail.getDescription())
                                    .build();

                            learningSearchTopics.addAll(courseDetail.getTopics().stream().map(
                                    topicDetail -> {
                                        LearningSearchDocument currTopic  = LearningSearchDocument.builder()
                                                .fieldId(topicDetail.getId())
                                                .title(topicDetail.getTitle())
                                                .contentType(ContentType.TOPIC)
                                                .path(courseDetail.getId()+"$"+topicDetail.getId())
                                                .build();


                                        learningSearchSubtopics.addAll(topicDetail.getSubtopics().stream().map(
                                                subtopicDetail -> {
                                                    LearningSearchDocument currSubtopic = LearningSearchDocument.builder()
                                                            .fieldId(subtopicDetail.getId())
                                                            .title(subtopicDetail.getTitle())
                                                            .contentType(ContentType.SUBTOPIC)
                                                            .content(subtopicDetail.getContent())
                                                            .path(courseDetail.getId()+"$"+topicDetail.getId()+"$"+subtopicDetail.getId())
                                                            .build();
                                                    return currSubtopic;
                                                }).collect(Collectors.toList()));

                                        return currTopic;
                                    }).collect(Collectors.toList()));

                            return currCourse;
                        }).collect(Collectors.toList()));

                IndexCoordinates index = IndexCoordinates.of("learningsearchindex");

                if (!elasticsearchOperations.indexOps(index).exists()) {
                    elasticsearchOperations.indexOps(LearningSearchDocument.class).create();
                    elasticsearchOperations.indexOps(LearningSearchDocument.class).putMapping(
                            elasticsearchOperations.indexOps(LearningSearchDocument.class).createMapping()
                    );
                }

                elasticsearchOperations.save(learningSearchCourses, IndexCoordinates.of("learningsearchindex"));
                elasticsearchOperations.save(learningSearchTopics, IndexCoordinates.of("learningsearchindex"));
                elasticsearchOperations.save(learningSearchSubtopics, IndexCoordinates.of("learningsearchindex"));

                log.info("Insert Data for Elastic Search Ended");
            }

        } catch(Exception  exception){
            log.error("Error while saving seed data {} ", exception.getMessage());
        }
    }

    private boolean insertSeedDataInDb(){
        return courseRepository.findAll().isEmpty();
    }

    private boolean insertInElasticSearch(){
        IndexCoordinates index = IndexCoordinates.of("learningsearchindex");
        return !elasticsearchOperations.indexOps(index).exists() || elasticsearchOperations.count(Query.findAll(), LearningSearchDocument.class)==0;
    }
}
