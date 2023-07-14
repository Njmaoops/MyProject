package com.example.blog_system_ssm.service;

import com.example.blog_system_ssm.entity.ArticleInfo;
import com.example.blog_system_ssm.mapper.ArticleMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date 2023/5/19 9:35
 */
@Service
public class ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    public int getArtCountByUid(Integer uid) {
        return articleMapper.getArtCountByUid(uid);
    }

    public List<ArticleInfo> getMyList(Integer uid) {
        return articleMapper.getMyList(uid);
    }

    public Integer deleteArtById(Integer id, Integer uid) {
        return articleMapper.deleteArtById(id, uid);
    }

    public ArticleInfo getArtById(Integer id) {
        return articleMapper.getArtById(id);
    }

    public int updateRCountById(Integer id) {
        return articleMapper.updateRCountById(id);
    }

    public int addArt(ArticleInfo articleInfo) {
        return articleMapper.addArt(articleInfo);
    }

    public List<ArticleInfo> getAllArtList() {
        return articleMapper.getAllArtList();
    }

    public int updateArtById(ArticleInfo articleInfo) {
        return articleMapper.updateArtById(articleInfo);
    }

    public List<ArticleInfo> getListByPage(@Param("psize") Integer psize, @Param("offset") Integer offset) {
        return articleMapper.getListByPage(psize, offset);
    }

    public int getCount() {
        return articleMapper.getCount();
    }
}
