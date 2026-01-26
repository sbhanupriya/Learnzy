package com.learnzy.backend.service;

import com.learnzy.backend.entity.Course;
import com.learnzy.backend.models.CourseDetail;
import com.learnzy.backend.repository.CourseRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public void getAllCourses(){
        List<Course> courseList = courseRepository.findAll();
    }



    public CourseDetail getCourseById(String courseId){
//        Optional<Course> course = courseRepository.findByCourseCode(courseId).orElseThrow();
        return new CourseDetail();
    }
}
