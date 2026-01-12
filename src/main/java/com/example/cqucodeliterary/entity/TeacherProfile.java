package com.example.cqucodeliterary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "teacher_profiles")
@Data
@NoArgsConstructor
public class TeacherProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username; // 与 User 表关联

    private String realName;    // 真实姓名
    private String title;       // 职称 (如: 教授、副教授)
    private String office;      // 办公室地点
    private String bio;         // 个人简介
}