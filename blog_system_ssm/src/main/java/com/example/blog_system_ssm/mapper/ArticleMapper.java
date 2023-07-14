package com.example.blog_system_ssm.mapper;

import com.example.blog_system_ssm.entity.ArticleInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Date 2023/5/19 9:20
 */
@Mapper
public interface ArticleMapper {
    /**
     * 获得某个user用户的文章总数
     */
    int getArtCountByUid(Integer uid);

    /**
     * 查看当前用户所有文章
     *
     * @param uid
     * @return
     */
    List<ArticleInfo> getMyList(Integer uid);

    int deleteArtById(Integer id, Integer uid);

    //通过文章id 来获取详情
    ArticleInfo getArtById(Integer id);

    // 通过文章id 获得阅读量
    int updateRCountById(Integer id);

    // 添加文章
    int addArt(ArticleInfo articleInfo);


    // 获取所有用户的文章
    List<ArticleInfo> getAllArtList();

    // 修改当前文章信息
    int updateArtById(ArticleInfo articleInfo);

    // 获取分页文章的信息内容:
    List<ArticleInfo> getListByPage(Integer psize, Integer offset);

    int getCount();
}
