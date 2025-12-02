package com.zero.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.blog.dto.article.PublishArticleActionDto;
import com.zero.blog.entity.*;
import com.zero.blog.exception.CommonException;
import com.zero.blog.service.ArticleService;
import com.zero.blog.mapper.ArticleMapper;
import com.zero.blog.service.ArticleTagService;
import com.zero.blog.service.ArticleToTagService;
import com.zero.blog.service.CommentService;
import com.zero.blog.util.CommonResult;
import com.zero.blog.vo.ArticleVo;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService{

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleTagService articleTagService;
    @Autowired
    ArticleToTagService articleToTagService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private CommentService commentService;

    @Override
    public Integer ArticleCount() {
        Long l = articleMapper.selectCount(null);
        return Integer.valueOf(String.valueOf(l));
    }

    @Override
    public IPage<ArticleVo> selectPage(IPage<ArticleVo> page, String articleTitle) {
        return articleMapper.articleList(page,articleTitle);
    }

    @Override
    public List<Article> getArticleByUserId(String userId) {
        return articleMapper.selectList(new QueryWrapper<Article>().eq("user_Id", userId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult publishArticleAction(HttpServletRequest request, PublishArticleActionDto publishArticleActionDto) {
        //保存文章
        User user = (User) request.getSession().getAttribute("user");
        Article article = new Article();
        article.setArticleId(publishArticleActionDto.getArticleId());
        article.setArticleCoverUrl(publishArticleActionDto.getArticleCoverUrl());
        article.setUserId(user.getUserId());
        article.setArticleTitle(publishArticleActionDto.getArticleTitle());
        if (StrUtil.isBlank(article.getArticleId())) {
            article.setArticleCreateTime(DateUtil.date());
        }
        article.setArticleContext(publishArticleActionDto.getArticleContext());
        article.setArticlePraiseNum(0);
        article.setArticleBrowseTime(0);

        if (!saveOrUpdate(article)) {
            System.out.println(article);
            return CommonResult.failed("操作失败，请刷新页面重试!");
        }

        //保持文章的标签
        String[] articleTagIds = publishArticleActionDto.getArticleTagIds();
        if (Objects.nonNull(articleTagIds) && articleTagIds.length > 0) {
            //删除原先的标签数据
            articleTagService.remove(Wrappers.<ArticleTag>lambdaQuery().eq(ArticleTag::getArticleTagId, article.getArticleId()));
        }

        ArrayList<ArticleToTag> articleTagLists = new ArrayList<>();
        for (String articleTagId : articleTagIds) {
            ArticleToTag articleToTag = new ArticleToTag();
            articleToTag.setArticleId(article.getArticleId());
            articleToTag.setArticleTagId(articleTagId);
            articleTagLists.add(articleToTag);
        }
        if (!articleToTagService.saveBatch(articleTagLists, 50)) {
            throw new CommonException("操作文章失败，保存文章标签失败");
        }

        servletContext.removeAttribute("articleTypeList");

        return CommonResult.success("操作成功");
    }

    public IPage<ArticleVo> userArticleList(IPage<ArticleVo> articlePage, String articleTitle, String userId) {
        return articleMapper.userArticleList(articlePage, articleTitle, userId);
    }

    public List<Article> hotArticle(int num){
        return articleMapper.hotArticleList(num);
    }


    public ArticleVo getArticle(String id){
        return articleMapper.getArticle(id);
    }

    public IPage<ArticleVo> tagArticleList(Page<ArticleVo> articlePage, String articleTagId) {
        return articleMapper.tagArticleList(articlePage, articleTagId);
    }

    @Transactional(rollbackFor = Exception.class)
    public CommonResult delArticle(String articleId){
        if (!StrUtil.isBlank(articleId)){
            if(articleMapper.deleteById(articleId)==0){
                return CommonResult.failed("删除失败");
            }
            if (commentService.list(Wrappers.<Comment>lambdaQuery().eq(Comment::getArticleId,articleId)).size()>=1){
                if (!commentService.remove(Wrappers.<Comment>lambdaQuery().eq(Comment::getArticleId,articleId))){
                    throw new CommonException("操作文章失败，保存文章标签失败");
                }
            }
            return CommonResult.success("删除成功");
        }
        return CommonResult.failed("删除失败");
    }
}




