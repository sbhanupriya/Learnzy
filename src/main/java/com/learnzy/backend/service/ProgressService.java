package com.learnzy.backend.service;

import com.learnzy.backend.entity.Enrollment;
import com.learnzy.backend.entity.Progress;
import com.learnzy.backend.entity.Subtopic;
import com.learnzy.backend.entity.Users;
import com.learnzy.backend.exception.ResourceNotFoundException;
import com.learnzy.backend.exception.UserNotEnrolledException;
import com.learnzy.backend.models.progress.CompleteSubtopicResponse;
import com.learnzy.backend.models.progress.CompletedSubtopicDetail;
import com.learnzy.backend.models.progress.ProgressResponse;
import com.learnzy.backend.repository.EnrollmentRepository;
import com.learnzy.backend.repository.ProgressRepository;
import com.learnzy.backend.repository.SubtopicRepository;
import com.learnzy.backend.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgressService {

    @Autowired
    private ProgressRepository progressRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubtopicRepository subtopicRepository;

    public ProgressResponse trackProgress(Long enrollmentId, Long userId){

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow(() ->
                new UserNotEnrolledException(String.format("You are not enrolled")));

        if(!enrollment.getUser().getId().equals(userId))
            throw new UserNotEnrolledException(String.format("You are not enrolled in {}", enrollment.getCourse().getCourseCode()));


        Long subtopicCount = enrollment.getCourse().getTopicList().stream().mapToLong(topic-> topic.getSubtopicList().size()).sum();

        List<CompletedSubtopicDetail> completedSubtopicDetailList = enrollment.getProgressList().stream().map(progress -> {

            CompletedSubtopicDetail completedSubtopicDetail = CompletedSubtopicDetail.builder()
                    .subtopicId(progress.getSubtopic().getSubtopicCode())
                    .completedAt(progress.getCreatedOn())
                    .build();

            return completedSubtopicDetail;
        }).collect(Collectors.toList());

        Long completedSubtopicCount = (long)completedSubtopicDetailList.size();
        BigDecimal percentage = BigDecimal.valueOf(completedSubtopicCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(subtopicCount), 2, RoundingMode.HALF_UP);


        ProgressResponse progressResponse = ProgressResponse.builder()
                .courseId(enrollment.getCourse().getCourseCode())
                .courseTitle(enrollment.getCourse().getTitle())
                .enrollmentId(enrollmentId)
                .totalSubtopics(subtopicCount)
                .completedSubtopics(completedSubtopicCount)
                .completedItems(completedSubtopicDetailList)
                .completionPercentage(percentage)
                .build();

        return progressResponse;
    }

    public CompleteSubtopicResponse markSubtopicCompleted(String subtopicId, Long userId){

       Users user =  userRepository.getReferenceById(userId);

       Enrollment found = user.getEnrollmentList().stream()
                .filter(enrollment -> enrollment.getCourse().getTopicList().stream()
                        .anyMatch(topic -> topic.getSubtopicList().stream().anyMatch(subtopic ->
                                subtopic.getSubtopicCode().equalsIgnoreCase(subtopicId))))
                .findFirst().orElseThrow(() -> new UserNotEnrolledException(String.format("You are not enrolled")));


       Subtopic subtopicCompleted = found.getCourse().getTopicList().stream()
                .flatMap(topic -> topic.getSubtopicList().stream())
                .filter(s -> s.getSubtopicCode().equalsIgnoreCase(subtopicId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Subtopic {} not found", subtopicId)));

        Progress progress = Progress.builder()
                .subtopic(subtopicCompleted)
                .enrollment(found)
                .build();

        progressRepository.save(progress);

        CompleteSubtopicResponse completeSubtopicResponse = CompleteSubtopicResponse.builder()
               .completedAt(progress.getCreatedOn())
                .subtopicId(subtopicId)
                .completed(Boolean.TRUE)
               .build();

        return completeSubtopicResponse;
    }
}
