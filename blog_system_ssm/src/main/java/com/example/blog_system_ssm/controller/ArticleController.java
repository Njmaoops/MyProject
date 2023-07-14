package com.example.blog_system_ssm.controller;

import com.example.blog_system_ssm.common.AjaxResult;
import com.example.blog_system_ssm.common.ContentUtils;
import com.example.blog_system_ssm.common.UserSessionUtils;
import com.example.blog_system_ssm.entity.ArticleInfo;
import com.example.blog_system_ssm.entity.UserInfo;
import com.example.blog_system_ssm.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * @Date 2023/5/19 12:44
 */
@RestController
@RequestMapping("/art")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @RequestMapping("/mylist")
    public AjaxResult myList(HttpServletRequest request) {
        UserInfo userinfo = UserSessionUtils.getSessionUser(request);
        if (userinfo == null) {
            return AjaxResult.fail(-1, "非法参数");
        }
        List<ArticleInfo> artList = articleService.getMyList(userinfo.getId());
        // 对内容进行处理
        ContentUtils.handleContent(artList);
        return AjaxResult.success(artList);
    }

    @RequestMapping("/delete")
    public AjaxResult deleteArtById(HttpServletRequest request, Integer id) {
        if (id == null || id <= 0) {
            return AjaxResult.fail(-1, "参数非法");
        }
        UserInfo userInfo = UserSessionUtils.getSessionUser(request);
        if (userInfo == null || userInfo.getId() == null) {
            return AjaxResult.fail(-1, "钓鱼执法");
        }
        int rows = articleService.deleteArtById(id, userInfo.getId());
        return AjaxResult.success(rows);
    }

    @RequestMapping("/detail")
    public AjaxResult getArtById(Integer id) {
        // 对 Id 进行合法性的判断
        if (id == null || id <= 0) {
            return AjaxResult.fail(-1, "参数非法");
        }
        ArticleInfo articleInfo = articleService.getArtById(id);
        System.out.println(articleInfo);
        // 对获取的文章进行判断合法性
        if (articleInfo == null) {
            // 200 ,"" , articleInfo
            return AjaxResult.fail(-1, "不存在该文章??");
        }
        return AjaxResult.success(articleInfo);
    }

    @RequestMapping("/rcount")
    public AjaxResult updateRCountById(Integer id) {
        if (id == null || id <= 0) {
            return AjaxResult.fail(-1, "文章id不对");
        }
        articleService.updateRCountById(id);
        ArticleInfo articleInfo = articleService.getArtById(id);
        Integer rcount = articleInfo.getRcount();
        return AjaxResult.success(rcount);
    }

    @RequestMapping("/add")
    public AjaxResult addArt(HttpServletRequest request, ArticleInfo articleInfo) {
        // 非空校验
        if (articleInfo == null
                || !StringUtils.hasLength(articleInfo.getTitle())
                || !StringUtils.hasLength(articleInfo.getContent())) {
            return AjaxResult.fail(-1, "非法参数");
        }
        // 缺少uid信息
        // 但是添加操作一定会有用户,因此可以获取Session
        UserInfo userInfo = UserSessionUtils.getSessionUser(request);
        if (userInfo == null || userInfo.getId() <= 0) {
            return AjaxResult.fail(-1, "非法参数");
        }
        // 从session获取的user对象 来进行
        articleInfo.setUid(userInfo.getId());
        int rows = articleService.addArt(articleInfo);
        // 1
        return AjaxResult.success(rows);
    }

    @RequestMapping("/alllist")
    public AjaxResult getAllArtList() {
        return AjaxResult.success(articleService.getAllArtList());
    }


    @RequestMapping("/update")
    public AjaxResult updateArtById(ArticleInfo articleInfo) {
        // 存在 id , title , content
        // 非空检验
        if (articleInfo == null || articleInfo.getId() <= 0
                || !StringUtils.hasLength(articleInfo.getTitle())
                || !StringUtils.hasLength(articleInfo.getContent())) {
            return AjaxResult.fail(-1, "非法参数");
        }
        // 进行更新操作
        articleInfo.setUpdatetime(LocalDateTime.now());
        // 还需要更新设置state 状态
        int rows = articleService.updateArtById(articleInfo);
        return AjaxResult.success(rows);
    }

    /**
     * 接收两个参数 ,一个参数是当前的页码,另一个参数是offset , 计算公式如下 (pindex - 1) * psize = offset
     *
     * @param pindex 是显示页面的位置 ( 第几页 )
     * @param psize  是显示页面的最大数 ( limit = ? )
     * @return
     */
    @RequestMapping("/listbypage")
    public AjaxResult getListByPage(Integer pindex, Integer psize) {
        if (pindex == null || pindex <= -1) {
            pindex = 1;
        }
        if (psize == null || psize <= 1) {
            // 默认两条博客进行换页
            psize = 2;
        }
        Integer offset = (pindex - 1) * psize;
        // 获取文章列表
        List<ArticleInfo> list = articleService.getListByPage(psize, offset);
        // 进行内容处理
        ContentUtils.handleContent(list);
        // 找到当前列表存在多少页面:
        int totalCount = articleService.getCount();
        // 总条数 / psize  -> (每一页显示的条数)
        double pcountdb = totalCount / (psize * 1.0);
        // 向上取整 , 获得最大可以存在的页数
        int pcount = (int) Math.ceil(pcountdb);
        HashMap<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("pcount", pcount);

        return AjaxResult.success(result);
    }
}
