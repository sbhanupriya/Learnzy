package com.learnzy.backend.service;

import com.learnzy.backend.entity.*;
import com.learnzy.backend.exception.custom.ForbiddenException;
import com.learnzy.backend.exception.custom.ResourceNotFoundException;
import com.learnzy.backend.models.progress.CompleteSubtopicResponse;
import com.learnzy.backend.models.progress.CompletedSubtopicDetail;
import com.learnzy.backend.models.progress.ProgressResponse;
import com.learnzy.backend.repository.EnrollmentRepository;
import com.learnzy.backend.repository.ProgressRepository;
import com.learnzy.backend.repository.SubtopicRepository;
import com.learnzy.backend.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
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
                new ResourceNotFoundException(String.format("Enrollment %s not found", enrollmentId)));

        if(!enrollment.getUser().getId().equals(userId)){
            log.error(String.format("Track progress failed for user %s as not enrolled with id %s",userId, enrollmentId));
            throw new ForbiddenException(String.format("You are not authorized to view this enrollment"));
        }

        Long subtopicCount = enrollment.getCourse().getTopicList().stream().mapToLong(topic-> topic.getSubtopicList().size()).sum();

        List<CompletedSubtopicDetail> completedSubtopicDetailList = enrollment.getProgressList().stream().map(progress -> {
            CompletedSubtopicDetail completedSubtopicDetail = CompletedSubtopicDetail.builder()
                    .subtopicId(progress.getSubtopic().getSubtopicCode())
                    .subtopicTitle(progress.getSubtopic().getTitle())
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

        Subtopic subtopicCompleted = subtopicRepository.findBySubtopicCodeIgnoreCase(subtopicId).orElseThrow(() ->
                new ResourceNotFoundException(String.format("Subtopic %s not found", subtopicId)));

        Users user =  userRepository.getReferenceById(userId);

       Enrollment found = user.getEnrollmentList().stream()
                .filter(enrollment -> enrollment.getCourse().getTopicList().stream()
                        .anyMatch(topic -> topic.getSubtopicList().stream().anyMatch(subtopic ->
                                subtopic.getSubtopicCode().equalsIgnoreCase(subtopicId))))
                .findFirst().orElseThrow(() -> new ForbiddenException(String.format("You must be enrolled in this course to mark subtopics as complete")));


       Progress progress = Progress.builder()
                .subtopic(subtopicCompleted)
                .enrollment(found)
                .build();

        progressRepository.save(progress);


        log.info(String.format("User %s marked subtopic %s as complete under course %s", user.getId(), subtopicId, found.getCourse().getCourseCode()));

        CompleteSubtopicResponse completeSubtopicResponse = CompleteSubtopicResponse.builder()
               .completedAt(progress.getCreatedOn())
                .subtopicId(subtopicId)
                .completed(Boolean.TRUE)
               .build();

        return completeSubtopicResponse;
    }
}
