package com.example.cqucodeliterary.repository;

import com.example.cqucodeliterary.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Integer> {
    // 通过用户名查找学生档案
    Optional<StudentProfile> findByUsername(String username);

    // 判断该学生的档案是否存在
    boolean existsByUsername(String username);
}