package com.example.blog_system_ssm.mapper;

import com.example.blog_system_ssm.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Date 2023/5/18 15:44
 */
@Mapper
public interface UserMapper {
    // 进行注册功能
    int addUser(UserInfo userInfo);

    // 登录功能
    UserInfo getUserByName(@Param("username") String username);

    //根据id查询user用户
    UserInfo getUserById(@Param("id") Integer id);

    // 根据用户 id冻结账户
    Integer freezeUserInfo(Integer id);

    Integer clearError(Integer id);

    Integer upError(Integer id);

    Integer changePassword(Integer id, String password);

    Integer changeNickName(Integer id, String nickname);

    Integer setSecurityInfo(String securityQuestion, String securityAnswer, Integer id);

    Integer setAllInfo(UserInfo userInfo);

    UserInfo getUserBySecurityQuestion(String securityquestion);

    Integer setPasswordById(UserInfo userInfo);
}
