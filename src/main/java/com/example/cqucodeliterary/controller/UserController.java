package com.example.cqucodeliterary.controller;

import com.example.cqucodeliterary.entity.StudentProfile;
import com.example.cqucodeliterary.entity.TeacherProfile;
import com.example.cqucodeliterary.entity.User;
import com.example.cqucodeliterary.repository.StudentProfileRepository;
import com.example.cqucodeliterary.repository.TeacherProfileRepository;
import com.example.cqucodeliterary.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 允许前端跨域访问
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        Map<String, Object> response = new HashMap<>();

        if (userRepository.findByUsername(username).isPresent()) {
            response.put("success", false);
            response.put("message", "用户名已存在");
            return response;
        }

        // 关键点：不要在这里设置 role，让它在数据库里保持为 null
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        // newUser.setRole(null); // 默认就是 null

        userRepository.save(newUser);

        response.put("success", true);
        response.put("message", "注册成功");
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        Map<String, Object> response = new HashMap<>();

        return userRepository.findByUsername(username)
                .map(user -> {
                    if (user.getPassword().equals(password)) {
                        response.put("success", true);
                        response.put("role", user.getRole());
                        return response;
                    } else {
                        response.put("success", false);
                        response.put("message", "密码错误");
                        return response;
                    }
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("message", "账号不存在");
                    return response;
                });
    }

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private TeacherProfileRepository teacherProfileRepository;

    @PostMapping("/rolechoose")
    @Transactional // 保证两张表的操作同步成功
    public Map<String, Object> roleChoose(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String role = params.get("role").toUpperCase();
        Map<String, Object> response = new HashMap<>();

        return userRepository.findByUsername(username).map(user -> {
            // 1. 更新主表角色
            user.setRole(role);
            userRepository.save(user);

            // 2. 根据角色初始化不同的扩展表
            if ("STUDENT".equals(role)) {
                if (!studentProfileRepository.findByUsername(username).isPresent()) {
                    StudentProfile sp = new StudentProfile();
                    sp.setUsername(username);
                    studentProfileRepository.save(sp);
                }
            } else if ("TEACHER".equals(role)) {
                if (!teacherProfileRepository.findByUsername(username).isPresent()) {
                    TeacherProfile tp = new TeacherProfile();
                    tp.setUsername(username);
                    teacherProfileRepository.save(tp);
                }
            }

            response.put("success", true);
            return response;
        }).orElseGet(() -> {
            response.put("success", false);
            response.put("message", "用户不存在");
            return response;
        });
    }
}