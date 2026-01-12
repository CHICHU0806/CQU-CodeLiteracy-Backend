package com.example.cqucodeliterary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data  // 自动生成 Getter/Setter/toString
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // 角色字段：STUDENT 或 TEACHER
    private String role;

    @Column(name = "total_scores")
    private Integer totalScores = 0; // 总得分

    @Column(name = "continuous_days")
    private Integer continuousDays = 0; // 连续签到天数

    @Column(name = "last_check_in_date")
    private LocalDate lastCheckInDate; // 上次签到日期

    // 注册时使用的构造方法
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.totalScores = 0;
        this.continuousDays = 0;
    }
}