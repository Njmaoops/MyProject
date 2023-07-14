package com.example.blog_system_ssm.entity.vo;

import com.example.blog_system_ssm.entity.UserInfo;
import lombok.Data;

/**
 * @Date 2023/5/19 9:37
 */

//user的视图类
@Data
public class UserInfoVO extends UserInfo {
    private Integer articleCount; // user发表的文章总数
}
