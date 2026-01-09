package com.example.cqucodeliterary;

/**
 * 这是一个标准的 Java 实体类
 * 它代表了前端页面侧边栏需要展示的用户数据结构
 */
public class User {
    // 1. 私有属性：封装数据，不让外部直接修改
    private String name;
    private String role;
    private String avatar;

    // 2. 无参构造方法：Spring 内部通过反射创建对象时必须用到它
    public User() {
    }

    // 3. 全参构造方法：方便我们在后端直接 new 出一个带数据的对象
    public User(String name, String role, String avatar) {
        this.name = name;
        this.role = role;
        this.avatar = avatar;
    }

    // 4. Getter 方法：这是 Spring 将对象转为 JSON 的关键“开关”
    // Spring 默认会根据 getXXX 的名字来生成 JSON 的键名
    public String getName() { return name; }
    public String getRole() { return role; }
    public String getAvatar() { return avatar; }

    // 5. Setter 方法：以后从数据库读取数据填充对象时会用到
    public void setName(String name) { this.name = name; }
    public void setRole(String role) { this.role = role; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
}