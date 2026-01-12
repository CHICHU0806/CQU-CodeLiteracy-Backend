package com.example.cqucodeliterary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false) // 课程名不能为空
    private String name;

    @Column(length = 500) // 给描述留足空间
    private String description;

    @Column(nullable = false) // 必须知道是哪个老师创建的
    private String teacher;

    // 统计字段，标记为 @Transient 确保不进数据库
    @Transient
    private Integer studentCount = 0;
    @Transient
    private Integer materialCount = 0;
}