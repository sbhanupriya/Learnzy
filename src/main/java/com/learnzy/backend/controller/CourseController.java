package com.learnzy.backend.controller;

import com.learnzy.backend.models.CourseDetail;
import com.learnzy.backend.models.CourseSummaryResponse;
import com.learnzy.backend.models.EnrollmentResponse;
import com.learnzy.backend.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDetail> getCourseDetail(@PathVariable String courseId) {
       CourseDetail courseDetail =  courseService.getCourseDetail(courseId);
       return new ResponseEntity<>(courseDetail, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<CourseSummaryResponse> getAll(){
       CourseSummaryResponse courseSummaryResponse =  courseService.getAllCourses();
       return new ResponseEntity<>(courseSummaryResponse, HttpStatus.OK);
    }

    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<EnrollmentResponse> enroll(@PathVariable String courseId, Authentication authentication){
        EnrollmentResponse enrollmentResponse = courseService.enroll(courseId, (Long) authentication.getPrincipal());
        return new ResponseEntity<>(enrollmentResponse, HttpStatus.CREATED);
    }
}
