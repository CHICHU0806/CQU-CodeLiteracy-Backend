package com.example.cqucodeliterary.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 1. 映射资料文件夹：
         * 当访问 http://localhost:8080/materials/test.pdf 时
         * 系统会去磁盘的 D:/my_materials/ 目录下找 test.pdf
         */
        registry.addResourceHandler("/materials/**")
                // 注意：file: 后面跟的是你电脑上的绝对路径，最后一定要带斜杠
                .addResourceLocations("file:D:/Owner/Java/CQU-CodeLiterary/materials/");

        /**
         * 2. 如果你的头像也想通过 URL 访问，可以再加一个：
         */
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:D:/cqu_project/avatars/");
    }
}