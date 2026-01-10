package com.example.cqucodeliterary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
public class UserController {

    // 1. 注入数据库操作的“遥控器”
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/api/user/info")
    public User getUserInfo() {
        // 修改这里：现在 User 的构造函数变了
        User user = new User();
        user.setName("周怀涛"); // 这里的名字可以根据逻辑设置
        user.setRole("学生");
        user.setAvatar("电力涉网设备.png");
        return user;
    }

    // --- 注册接口：真正把数据存进 SQLite ---
    @PostMapping("/api/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 保存到 SQLite 数据库
            userRepository.save(user);

            response.put("success", true);
            response.put("message", "注册成功！已存入数据库");
            System.out.println("【数据库通知】新用户注册：" + user.getUsername());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "注册失败：" + e.getMessage());
        }
        return response;
    }

    @GetMapping("/api/courses")
    public List<Course> getCourses() {
        List<Course> list = new ArrayList<>();
        list.add(new Course("Java 后端开发", "学习 Spring Boot 核心原理与 API 设计"));
        list.add(new Course("前端基础建设", "掌握 HTML5, CSS3 与 JavaScript 交互"));
        list.add(new Course("数据库建模", "理解关系型数据库与 SQL 优化"));
        list.add(new Course("计算机网络", "深入 HTTP 协议与 TCP/IP 模型"));
        list.add(new Course("软件工程实践", "团队协作与版本控制 Git 的使用"));
        return list;
    }

    private int enrollCount = 0;
    @PostMapping("/api/courses/enroll")
    public Map<String, Object> enroll(@RequestBody Map<String, String> data) {
        String courseTitle = data.get("title");
        enrollCount++;
        Map<String, Object> response = new HashMap<>();
        response.put("message", "报名成功！");
        response.put("newCount", enrollCount);
        return response;
    }
}