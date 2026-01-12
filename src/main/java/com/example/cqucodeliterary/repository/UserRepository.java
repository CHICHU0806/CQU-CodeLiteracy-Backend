package com.example.cqucodeliterary.repository;

import com.example.cqucodeliterary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // 使用 Optional 可以优雅地处理查不到用户的情况，避免直接返回 null
    Optional<User> findByUsername(String username);

    // 查找所有特定角色的用户（用于学生管理列表）
    List<User> findAllByRole(String role);

    // 统计特定角色的用户数量（用于统计板）
    long countByRole(String role);
}