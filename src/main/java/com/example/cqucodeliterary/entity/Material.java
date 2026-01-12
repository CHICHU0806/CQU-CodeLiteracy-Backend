package com.example.cqucodeliterary.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "materials")
@Data
@NoArgsConstructor
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;    // 文件原始名称
    private String filePath;    // 文件在硬盘上的绝对路径或相对路径
    private Integer courseId;   // 关联的课程ID
    private LocalDateTime uploadTime;
}