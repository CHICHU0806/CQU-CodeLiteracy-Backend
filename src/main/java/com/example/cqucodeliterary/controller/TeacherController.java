package com.example.cqucodeliterary.controller;

import com.example.cqucodeliterary.entity.Course;
import com.example.cqucodeliterary.entity.Material;
import com.example.cqucodeliterary.entity.User;
import com.example.cqucodeliterary.repository.CourseRepository;
import com.example.cqucodeliterary.repository.MaterialRepository;
import com.example.cqucodeliterary.repository.StudentProfileRepository;
import com.example.cqucodeliterary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TeacherController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private MaterialRepository materialRepository;

    // 定义存储路径（实际开发建议配置在 application.properties）
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    /**
     * 1. 统计板数据 (对应前端 refreshDashboard)
     * 重点：Key 必须与前端 stats.courses, stats.students 严格一致
     */
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        Map<String, Object> data = new HashMap<>();

        // 动态查询数据库真实数量
        long courses = courseRepository.count();
        long students = userRepository.countByRole("STUDENT");

        data.put("courses", courses);      // 前端 stats.courses
        data.put("students", students);    // 前端 stats.students
        data.put("totalCheckins", 0);      // 初始设为0，等 CheckInRecord 表做好了再求和

        // 返回空列表防止前端 updateRecentCourses 遍历报错
        data.put("recentCourses", new ArrayList<>());

        return data;
    }

    /**
     * 2. 学生管理列表 (对应前端 loadStudents)
     */
    @GetMapping("/students")
    public List<Map<String, Object>> getStudents(@RequestParam(defaultValue = "STUDENT") String role) {
        // 1. 找出所有学生账号
        List<User> userList = userRepository.findAllByRole("STUDENT");

        // 2. 组装前端需要的视图数据
        return userList.stream().map(user -> {
            Map<String, Object> view = new HashMap<>();
            view.put("username", user.getUsername());
            view.put("name", user.getUsername()); // 以后可以在 Profile 里加真实姓名

            // 关联学生档案表获取业务积分
            studentProfileRepository.findByUsername(user.getUsername()).ifPresentOrElse(profile -> {
                view.put("checkinCount", profile.getContinuousDays()); // 签到次数
                view.put("totalScore", profile.getTotalScores());      // 总分
            }, () -> {
                view.put("checkinCount", 0);
                view.put("totalScore", 0);
            });
            return view;
        }).collect(Collectors.toList());
    }

    /**
     * 3. 课程管理 (对应前端 loadCourses 和 createCourse)
     */
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        try {
            // 1. 简单的逻辑校验
            if (course.getName() == null || course.getName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // 2. 执行保存（JPA 会自动生成 SQL：INSERT INTO courses...）
            Course savedCourse = courseRepository.save(course);

            // 3. 返回 201 Created 状态码和保存后的对象（包含生成的 ID）
            return ResponseEntity.ok(savedCourse);
        } catch (Exception e) {
            // 如果保存失败（如数据库连接断开），返回 500
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/courses")
    public List<Course> getCourses() {
        // 关键：Sort.by 确保了数据在数据库层面就排好了序再发给前端
        return courseRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    // 在 TeacherController 类中添加
    @PostMapping("/courses/{courseId}/materials")
    public ResponseEntity<Map<String, Object>> uploadMaterial(
            @PathVariable Integer courseId,
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 确保物理目录存在
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) directory.mkdirs();

            // 2. 解决文件名冲突：使用时间戳前缀
            String originalFileName = file.getOriginalFilename();
            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
            Path path = Paths.get(UPLOAD_DIR + storedFileName);

            // 3. 物理保存文件到硬盘
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 4. 数据库记录保存
            Material material = new Material();
            material.setFileName(originalFileName);
            material.setFilePath(storedFileName); // 存文件名即可，路径由后端拼接
            material.setCourseId(courseId);
            material.setUploadTime(LocalDateTime.now());
            materialRepository.save(material);

            response.put("success", true);
            response.put("fileName", originalFileName);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "硬盘写入失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}