package com.example.blog_system_ssm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class BlogSystemSsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogSystemSsmApplication.class, args);
    }

}
