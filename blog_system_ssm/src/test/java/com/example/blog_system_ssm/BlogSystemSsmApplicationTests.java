package com.example.blog_system_ssm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class BlogSystemSsmApplicationTests {

    @Test
    void contextLoads() {
        // 创建加盐工具
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "123456";
        String finalPassword = passwordEncoder.encode(password);
        System.out.println("第一次加密" + passwordEncoder.encode(password));
        System.out.println("第二次加密" + passwordEncoder.encode(password));
        System.out.println("第三次加密" + passwordEncoder.encode(password));

        // 验证
        String inputPassword = "12345";
        String inputPassword2 = "123456";
        System.out.println("错误密码对比结果" +
                passwordEncoder.matches(inputPassword, finalPassword));
        System.out.println("错误密码对比结果" +
                passwordEncoder.matches(inputPassword2, finalPassword));
    }

}
