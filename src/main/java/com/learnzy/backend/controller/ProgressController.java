package com.learnzy.backend.controller;

import com.learnzy.backend.models.progress.CompleteSubtopicResponse;
import com.learnzy.backend.models.progress.ProgressResponse;
import com.learnzy.backend.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProgressController {
    @Autowired
    private ProgressService progressService;

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("enrollments/{enrollmentId}/progress")
    public ResponseEntity<ProgressResponse> trackProgress(@PathVariable Long enrollmentId,  Authentication authentication){
        ProgressResponse progressResponse = progressService.trackProgress(enrollmentId, (Long) authentication.getPrincipal());
        return new ResponseEntity<>(progressResponse, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("subtopics/{subtopicId}/complete")
    public ResponseEntity<CompleteSubtopicResponse> completeSubtopic(@PathVariable String subtopicId,  Authentication authentication){
        CompleteSubtopicResponse progressResponse = progressService.markSubtopicCompleted(subtopicId, (Long) authentication.getPrincipal());
        return new ResponseEntity<>(progressResponse, HttpStatus.OK);
    }

}

