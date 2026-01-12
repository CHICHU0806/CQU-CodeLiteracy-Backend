package com.example.cqucodeliterary.controller;

import com.example.cqucodeliterary.entity.User;
import com.example.cqucodeliterary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/study")
@CrossOrigin(origins = "*")
public class StudyController {

    @Autowired
    private UserRepository userRepository;

    // 1. 签到接口
    @PostMapping("/check-in")
    public Map<String, Object> checkIn(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        Map<String, Object> response = new HashMap<>();

        userRepository.findByUsername(username).ifPresentOrElse(user -> {
            LocalDate today = LocalDate.now();

            // 判断今天是否已经签到
            if (today.equals(user.getLastCheckInDate())) {
                response.put("success", false);
                response.put("message", "今日已签到，明天再来吧！");
                response.put("isTodayChecked", true);
            } else {
                // 签到逻辑：更新分数和连续天数
                if (user.getLastCheckInDate() != null && user.getLastCheckInDate().plusDays(1).equals(today)) {
                    user.setContinuousDays(user.getContinuousDays() + 1); // 连续了
                } else {
                    user.setContinuousDays(1); // 断签或首次签到，重置为1
                }

                user.setTotalScores(user.getTotalScores() + 10); // 每次签到加10分
                user.setLastCheckInDate(today);
                userRepository.save(user);

                response.put("success", true);
                response.put("message", "签到成功！积分+10");
                response.put("isTodayChecked", true);
            }
        }, () -> {
            response.put("success", false);
            response.put("message", "用户不存在");
        });

        return response;
    }

    // 2. 获取分数和状态接口 (对接前端 refreshCheckInStatus)
    @GetMapping("/score")
    public Map<String, Object> getScore(@RequestParam String username) {
        Map<String, Object> response = new HashMap<>();

        userRepository.findByUsername(username).ifPresentOrElse(user -> {
            response.put("totalScores", user.getTotalScores());
            response.put("continuousDays", user.getContinuousDays());

            // 判断今天是否已签到
            boolean isTodayChecked = LocalDate.now().equals(user.getLastCheckInDate());
            response.put("isTodayChecked", isTodayChecked);
        }, () -> {
            response.put("totalScores", 0);
            response.put("continuousDays", 0);
        });

        return response;
    }
}