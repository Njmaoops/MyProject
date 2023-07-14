package com.example.blog_system_ssm.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 图片绝对地址与虚拟地址的映射
 *
 * @Date 2023/6/28 16:03
 */

//@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        /**
         * 将 addResourceLocations 位置的图片 映射到 /static/image/** 位置
         */
        registry.addResourceHandler("/static/image/**").addResourceLocations("file:/g:/upload/");
        super.addResourceHandlers(registry);
    }
}
