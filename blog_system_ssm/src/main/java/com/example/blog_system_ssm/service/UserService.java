package com.example.blog_system_ssm.service;

import com.example.blog_system_ssm.entity.UserInfo;
import com.example.blog_system_ssm.mapper.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Date 2023/5/18 15:47
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public int reg(UserInfo userInfo) {
        return userMapper.addUser(userInfo);
    }

    public UserInfo getUserByName(@Param("username") String username) {
        return userMapper.getUserByName(username);
    }

    public UserInfo getUserBySecurityQuestion(String securityQuestion) {
        return userMapper.getUserBySecurityQuestion(securityQuestion);
    }

    public UserInfo getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    // 冻结账户功能
    public Integer freezeUserInfo(Integer id) {
        return userMapper.freezeUserInfo(id);
    }

    // 清除用户错误信息
    public Integer clearError(Integer id) {
        return userMapper.clearError(id);
    }

    // 更新用户密码错误次数
    public Integer upError(Integer id) {
        return userMapper.upError(id);
    }

    public Integer changePassword(Integer id, String password) {
        return userMapper.changePassword(id, password);
    }

    public Integer changeNickName(Integer id, String nickname) {
        return userMapper.changeNickName(id, nickname);
    }

    public Integer setSecurityInfo(String securityQuestion, String securityAnswer, Integer id) {
        return userMapper.setSecurityInfo(securityQuestion, securityAnswer, id);
    }

    public Integer setAllInfo(UserInfo userInfo) {
        return userMapper.setAllInfo(userInfo);
    }

    public Integer setPasswordById(UserInfo userInfo) {
        return userMapper.setPasswordById(userInfo);
    }
}
