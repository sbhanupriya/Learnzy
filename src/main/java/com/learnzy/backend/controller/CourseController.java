package com.learnzy.backend.controller;

import com.learnzy.backend.models.CourseDetail;
import com.learnzy.backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDetail> getCourseById(@PathVariable String courseId) {
       CourseDetail courseResponse =  courseService.getCourseById(courseId);
       return new ResponseEntity<>(courseResponse, HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<> getAll(){
//
//    }
}
