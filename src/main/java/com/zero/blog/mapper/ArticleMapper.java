package com.zero.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.blog.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.blog.vo.ArticleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 10904
 * @description 针对表【article】的数据库操作Mapper
 * @createDate 2024-02-02 01:01:05
 * @Entity com.zero.blog.entity.Article
 */
public interface ArticleMapper extends BaseMapper<Article> {

    IPage<ArticleVo> articleList(IPage<ArticleVo> articleVoIPage, @Param("articleTitle")String articleTitle);

    IPage<ArticleVo> userArticleList(IPage<ArticleVo> articlePage, String articleTitle, String userId);

    List<Article> hotArticleList(int num);

    ArticleVo getArticle(String articleId);

    IPage<ArticleVo> tagArticleList(Page<ArticleVo> articlePage, @Param("articleTagId") String articleTagId);

}




