package com.example.cqucodeliterary.controller;

import com.example.cqucodeliterary.entity.Course;
import com.example.cqucodeliterary.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/list")
    public List<Course> getAllCourses() {
        // 这行代码会把数据库里所有的课程找出来，并自动转成 JSON 格式
        return courseRepository.findAll();
    }
}