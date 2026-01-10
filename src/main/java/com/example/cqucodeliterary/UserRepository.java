package com.example.cqucodeliterary;

import com.example.cqucodeliterary.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // 继承后，你就自动拥有了 save(), findAll(), delete() 等功能
    // 甚至可以写一个：根据用户名找人的方法
    User findByUsername(String username);
}