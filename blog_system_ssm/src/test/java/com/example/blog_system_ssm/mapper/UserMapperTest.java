package com.example.blog_system_ssm.mapper;

import com.example.blog_system_ssm.entity.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Date 2023/5/18 16:00
 */
@SpringBootTest
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    @Transactional
    void addUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("Nva");
        userInfo.setPassword("123");
        int rows = userMapper.addUser(userInfo);
        System.out.println(rows);
    }

    @Test
    void getUserByName() {
        UserInfo userInfo = userMapper.getUserByName("admin");
        System.out.println(userInfo);
    }

    @Test
    void getUserById() {
        UserInfo userInfo = userMapper.getUserById(1);
        System.out.println(userInfo);
    }
}