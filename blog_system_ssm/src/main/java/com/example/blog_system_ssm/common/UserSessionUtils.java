package com.example.blog_system_ssm.common;

import com.example.blog_system_ssm.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 当前登录用户相关的操作
 *
 * @Date 2023/5/19 10:35
 */
public class UserSessionUtils {

    // 拿到用户的登录对象
    public static UserInfo getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        // 说明 session 不为空, 并且存在 user的相关session 会话属性
        if (session != null && session.getAttribute(AppVariable.USER_SESSION_KEY) != null) {
            return (UserInfo) session.getAttribute(AppVariable.USER_SESSION_KEY);

        }
        return null;
    }
}
