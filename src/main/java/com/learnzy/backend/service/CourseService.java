package com.learnzy.backend.service;

import com.learnzy.backend.entity.Course;
import com.learnzy.backend.entity.Enrollment;
import com.learnzy.backend.entity.Topic;
import com.learnzy.backend.entity.Users;
import com.learnzy.backend.enums.EnrollmentStatus;
import com.learnzy.backend.exception.ResourceNotFoundException;
import com.learnzy.backend.exception.UserAlreadyEnrolledException;
import com.learnzy.backend.models.*;
import com.learnzy.backend.repository.CourseRepository;
import com.learnzy.backend.repository.EnrollmentRepository;
import com.learnzy.backend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public CourseSummaryResponse getAllCourses() {
        List<Course> courseList = courseRepository.findAll();

        List<CourseSummary> courseSummaryList = courseList.stream()
                .map(course -> {

                    Long subtopicCount =  course.getTopicList().stream().mapToLong(topic-> topic.getSubtopicList().size()).sum();
                    int topicCount = course.getTopicList().size();

                    CourseSummary summary = CourseSummary.builder()
                            .id(course.getCourseCode())
                            .title(course.getTitle())
                            .description(course.getDescription())
                            .topicCount(topicCount)
                            .subtopicCount(subtopicCount)
                            .build();

                    return summary;
        }).collect(Collectors.toList());

        CourseSummaryResponse courseSummaryResponse =  CourseSummaryResponse.builder()
                .courses(courseSummaryList)
                .build();

        return courseSummaryResponse;

    }

    public CourseDetail getCourseDetail(String courseId){
        Course course = courseRepository.findByCourseCode(courseId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Course {} not found", courseId)));

        List<TopicDetail> topicDetailList = course.getTopicList().stream().map(topic -> {

            List<SubtopicDetail> subtopicDetailList =  topic.getSubtopicList().stream().map( subtopic -> {
                SubtopicDetail subtopicDetail =
                        SubtopicDetail.builder()
                                .id(subtopic.getSubtopicCode())
                                .title(subtopic.getTitle())
                                .content(subtopic.getContent())
                                .build();
                return subtopicDetail;
            }
            ).collect(Collectors.toList());

            TopicDetail subtopicDetail = TopicDetail.builder()
                    .subtopics(subtopicDetailList)
                    .title(topic.getTitle())
                    .id(topic.getTopicCode())
                    .build();

            return subtopicDetail;
        }).collect(Collectors.toList());

        CourseDetail courseDetail = CourseDetail.builder()
                .topics(topicDetailList)
                .description(course.getDescription())
                .title(course.getTitle())
                .id(course.getCourseCode())
                .build();

        return courseDetail;
    }

    public EnrollmentResponse enroll(String courseId, Long userId) {
        Course course = courseRepository.findByCourseCode(courseId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Course {} not found", courseId)));

        Users user = userRepository.getReferenceById(userId);

        Enrollment enrollment = enrollmentRepository.findByUserIdAndCourseId(userId, course.getId());

        if(enrollment!=null && enrollment.isActive())
            throw new UserAlreadyEnrolledException("You are already enrolled in this course");

        if(enrollment==null){
            enrollment = Enrollment.builder()
                    .user(user)
                    .course(course)
                    .status(EnrollmentStatus.ENROLLED)
                    .build();
        }

        enrollment.setStatus(EnrollmentStatus.ENROLLED);

        enrollmentRepository.save(enrollment);

        return EnrollmentResponse.builder()
                .enrollmentId(enrollment.getId())
                .courseId(course.getCourseCode())
                .courseTitle(course.getTitle())
                .enrolledAt(enrollment.getUpdatedDate())
                .build();

    }
}
