package com.example.blog_system_ssm.config;

import com.example.blog_system_ssm.common.AppVariable;
import com.example.blog_system_ssm.entity.UserInfo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 拦截器
 *
 * @Date 2023/5/18 18:54
 */
public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 统一登录处理 ( AOP 切面编程 )
     * 返回true -> 用户已经登录
     * false -> 用户未登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(AppVariable.USER_SESSION_KEY) != null) {
            // 不做任何处理
            System.out.println("当前用户为: " +
                    ((UserInfo) session.getAttribute(AppVariable.USER_SESSION_KEY)).getUsername()
            );
            return true;
        } else {
            // 跳转到登录页面
            System.out.println("拦截到页面,跳转login.html");
            response.sendRedirect("/blog_login.html");
            return false;
        }
    }
}
