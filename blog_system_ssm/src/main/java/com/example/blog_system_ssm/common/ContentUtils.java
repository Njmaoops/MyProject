package com.example.blog_system_ssm.common;

import com.example.blog_system_ssm.entity.ArticleInfo;

import java.util.List;

/**
 * @Date 2023/5/23 9:17
 */
public class ContentUtils {

    /**
     * 对list内的内容进行处理
     *
     * @param list
     */
    public static void handleContent(List<ArticleInfo> list) {
        for (ArticleInfo articleInfo : list) {
            // 内容大于50个字进行处理
            String content = articleInfo.getContent();
            if (content.length() > 50) {
                articleInfo.setContent(content.substring(0, 50));
            }
        }
    }
}
