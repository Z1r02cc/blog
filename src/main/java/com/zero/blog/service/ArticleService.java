package com.zero.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.blog.util.CommonResult;
import com.zero.blog.dto.article.PublishArticleActionDto;
import com.zero.blog.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.blog.vo.ArticleVo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


public interface ArticleService extends IService<Article> {

    Integer ArticleCount();

    IPage<ArticleVo> selectPage(IPage<ArticleVo> page,String articleTitle);


    List<Article> getArticleByUserId(String userId);

    CommonResult publishArticleAction(HttpServletRequest request, PublishArticleActionDto publishArticleActionDto);

    IPage<ArticleVo> userArticleList(IPage<ArticleVo> articlePage, String articleTitle, String userId);

    List<Article> hotArticle(int num);

    ArticleVo getArticle(String articleId);

    IPage<ArticleVo> tagArticleList(Page<ArticleVo> articlePage, String articleTagId);

    CommonResult delArticle(String articleId);
}
