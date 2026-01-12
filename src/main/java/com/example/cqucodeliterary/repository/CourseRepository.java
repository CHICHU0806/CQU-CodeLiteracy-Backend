package com.example.cqucodeliterary.repository;

import com.example.cqucodeliterary.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    // 关键方法：根据老师名字查他教的课
    List<Course> findByTeacher(String teacher);
}