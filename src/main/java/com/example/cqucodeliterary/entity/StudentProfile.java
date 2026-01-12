package com.example.cqucodeliterary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "student_profiles")
@Data
@NoArgsConstructor
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 关键：与 User 表的 username 建立逻辑关联
    @Column(unique = true, nullable = false)
    private String username;

    private String realName;      // 真实姓名
    private Integer totalScores = 0;
    private Integer continuousDays = 0;
    private LocalDate lastCheckInDate;
    private String department;    // 所属院系
}