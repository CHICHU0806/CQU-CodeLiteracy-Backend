package com.example.cqucodeliterary;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*") // 必须有这一行，允许 63342 访问 8080
@RestController
public class UserController {

    @GetMapping("/api/user/info")
    public User getUserInfo() {
        // 创建一个用户对象，填入前端 HTML 里显示的那个人的信息
        return new User("1157", "学生", "电力涉网设备.png");
    }

    // 注意：确保类上面依然有 @CrossOrigin
    @GetMapping("/api/courses")
    public List<Course> getCourses() {
        List<Course> list = new ArrayList<>();
        // 添加几条模拟数据
        list.add(new Course("Java 后端开发", "学习 Spring Boot 核心原理与 API 设计"));
        list.add(new Course("前端基础建设", "掌握 HTML5, CSS3 与 JavaScript 交互"));
        list.add(new Course("数据库建模", "理解关系型数据库与 SQL 优化"));
        list.add(new Course("计算机网络", "深入 HTTP 协议与 TCP/IP 模型"));
        list.add(new Course("软件工程实践", "团队协作与版本控制 Git 的使用"));
        return list;
    }

    private int enrollCount = 0;
    @PostMapping("/api/courses/enroll") // 👈 使用 PostMapping
    public Map<String, Object> enroll(@RequestBody Map<String, String> data) {
        String courseTitle = data.get("title");
        enrollCount++; // 报名人数加 1

        System.out.println("【后端】报名成功！当前总人数：" + enrollCount);

        // 构造一个 Map，既返回消息，也返回最新的数字
        Map<String, Object> response = new HashMap<>();
        response.put("message", "报名成功！");
        response.put("newCount", enrollCount);
        return response;
    }
}