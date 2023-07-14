package com.example.blog_system_ssm.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Date 2023/5/18 15:42
 */
@Data
// 这里需要让Userinfo支持序列化,不然不可以将user信息存储到redis中
public class UserInfo implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String photo;
    private LocalDateTime createtime;
    private LocalDateTime updatetime;
    private Integer state;
    // 该用户密码错误次数
    private Integer errorcount;
    //安全信息问题
    private String securityquestion;
    private String securityanswer;
    //昵称
    private String nickname;
    
}
