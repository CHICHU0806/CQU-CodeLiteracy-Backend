package com.example.cqucodeliterary.entity;

import jakarta.persistence.*; // 如果报错，请尝试改为 javax.persistence.*

/**
 * 升级后的实体类：既能对接前端展示，也能对接 SQLite 数据库
 */
@Entity
@Table(name = "users") // 指定在 SQLite 中生成的表名为 users
public class User {

    // 1. 必须有一个主键 ID，SQLite 会自动帮你从 1 开始数
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 2. 对应注册表单的数据
    @Column(unique = true) // 用户名不能重复
    private String username;
    private String password;

    // 3. 对应你之前的展示数据（可以先留着，或者以后完善）
    private String name;
    private String role;
    private String avatar;

    // --- 构造方法 ---
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.name = username; // 默认把用户名作为昵称
    }

    // --- Getter 和 Setter (必须要有，建议全部生成) ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}