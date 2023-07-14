package com.example.blog_system_ssm.mapper;

import com.example.blog_system_ssm.entity.ArticleInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Date 2023/5/19 9:24
 */
@SpringBootTest
class ArticleMapperTest {

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    void getArtCountByUid() {
        int rows = articleMapper.getArtCountByUid(1);
        System.out.println(rows);
    }

    @Test
    void getMyList() {
        List<ArticleInfo> list = articleMapper.getMyList(1);
        System.out.println(list);
    }

    @Test
    @Transactional
    void deleteArtById() {
        int result = articleMapper.deleteArtById(1, 1);
        if (result == 1) {
            System.out.println("删除成功, 事务发生回滚");
        }
    }

    @Test
    void getListByPage() {
        System.out.println(articleMapper.getListByPage(4, 2));
        System.out.println(articleMapper.getListByPage(4, 4));
    }
}