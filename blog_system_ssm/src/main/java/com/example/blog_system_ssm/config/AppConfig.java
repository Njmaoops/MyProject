package com.example.blog_system_ssm.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Date 2023/5/18 18:58
 */

@Configuration // 五大注解
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 将 登录拦截其添加到 registry内
        registry.addInterceptor(new LoginInterceptor())
                //添加拦截路径: 全部拦截 /**
                .addPathPatterns("/**")
                // 静态页面html外不拦截 其他的方法全部拦截,
                // 进行判断 是否没有完成登录请求, 如果没有完成, 就应该先完成登录
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/editor.md/**")
                .excludePathPatterns("/image/**")
                .excludePathPatterns("/js/**")
                // html部分不拦截 , 注册, 登录
                .excludePathPatterns("/reg.html")
                .excludePathPatterns("/blog_login.html")
                .excludePathPatterns("/reset_newPwd.html")
                // 因为要可以公开查看文章信息
                // 公开查看的blog不需要拦截
                .excludePathPatterns("/blog_list.html")
                .excludePathPatterns("/blog_detail.html")
                // 部分方法接口不拦:
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/reg")
                .excludePathPatterns("/user/confirmans")
                .excludePathPatterns("/user/resetpwd")
                .excludePathPatterns("/art/detail")
                .excludePathPatterns("/art/rcount")
                // 获取所有文章的请求不需要拦截
                .excludePathPatterns("/art/listbypage")
                .excludePathPatterns("/user/getcaptcha")
                .excludePathPatterns("/user/findques")
                .excludePathPatterns("/user/getuserbyid");

    }
}
