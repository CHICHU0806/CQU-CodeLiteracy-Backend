package com.example.cqucodeliterary.controller;

import com.example.cqucodeliterary.UserRepository;
import com.example.cqucodeliterary.entity.Course;
import com.example.cqucodeliterary.entity.User;
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

    @PostMapping("/api/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> data) {
        String username = (String) data.get("username");
        String password = (String) data.get("password");

        Map<String, Object> response = new HashMap<>();
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            response.put("success", true);
            response.put("message", "验证通过");

            // --- 老师在这里为你添加了 role 的返回逻辑 ---
            // 如果数据库里 role 是空的，这里自然就是 null
            response.put("role", user.getRole());

            System.out.println("用户[" + username + "]登录成功，当前身份为: " + user.getRole());
        } else {
            response.put("success", false);
            response.put("message", "账号或密码错误");
            response.put("role", null); // 登录失败明确返回 null
        }
        return response;
    }

    // --- 注册接口：真正把数据存进 SQLite ---
    @PostMapping("/api/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> data) {
        // 1. 先把注明的功能取出来
        String action = (String) data.get("action");
        String username = (String) data.get("username");
        String password = (String) data.get("password");

        System.out.println("【后端收到指令】当前行为：" + action);

        Map<String, Object> response = new HashMap<>();

        // 2. 增加一层逻辑保护：只有注明是 register 才会执行
        if (!"register".equals(action)) {
            response.put("success", false);
            response.put("message", "接口调用意图不明！");
            return response;
        }

        try {
            // 3. 执行具体的注册逻辑
            User user = new User(username, password);
            userRepository.save(user);

            response.put("success", true);
            response.put("message", "注册行为处理完毕！");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "处理失败: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/api/rolechoose")
    public Map<String, Object> roleChoose(@RequestBody Map<String, Object> data) {
        // 打印一下，看看前端到底传了什么过来
        System.out.println("【收到角色选择请求】原始数据: " + data);

        String username = (String) data.get("username");
        String role = (String) data.get("role");

        Map<String, Object> response = new HashMap<>();

        // 健壮性检查：如果 username 是空的，直接拦截
        if (username == null || username.isEmpty()) {
            response.put("success", false);
            response.put("message", "更新失败：用户名不能为空（请检查 localStorage）");
            return response;
        }

        try {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                user.setRole(role);
                userRepository.save(user);
                response.put("success", true);
                response.put("message", "身份确认成功");
            } else {
                response.put("success", false);
                response.put("message", "用户[" + username + "]不存在");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 在后端控制台打印堆栈，看看是不是数据库锁住了
            response.put("success", false);
            response.put("message", "数据库操作异常");
        }
        return response;
    }

    @PostMapping
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