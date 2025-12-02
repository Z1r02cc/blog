package com.zero.blog.intercepter;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zero.blog.entity.Article;
import com.zero.blog.entity.ArticleTag;
import com.zero.blog.entity.Link;
import com.zero.blog.service.ArticleService;
import com.zero.blog.service.ArticleTagService;
import com.zero.blog.service.LinkService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.List;

@Slf4j
public class GloballInterceptor implements HandlerInterceptor {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleTagService articleTagService;
    @Autowired
    private LinkService linkService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        ServletContext servletContext = request.getServletContext();

        //热门文章
        List<Article> articleHotList = (List<Article>) servletContext.getAttribute("articleHotList");
        if (CollUtil.isEmpty(articleHotList)) {
            articleHotList = articleService.hotArticle(5);
            servletContext.setAttribute("articleHotList", articleHotList);
        }

        //热门标签
        List<ArticleTag> articleTagList = (List<ArticleTag>) servletContext.getAttribute("articleTagList");
        if (CollUtil.isEmpty(articleTagList)) {
            articleTagList = articleTagService.list(Wrappers.<ArticleTag>lambdaQuery().select(ArticleTag::getArticleTagId, ArticleTag::getArticleTagName));
            servletContext.setAttribute("articleTagList", articleTagList);
        }

        String userAgent = request.getHeader("User-Agent") != null ? request.getHeader("User-Agent").toLowerCase() : "";
        boolean isMobile = userAgent.contains("mobile") || userAgent.contains("android") || userAgent.contains("iphone");
        // 将结果传递给模板
        servletContext.setAttribute("isMobile",isMobile);

        //友情连接
        List<Link> linkList = (List<Link>) servletContext.getAttribute("linkList");
        if (CollUtil.isEmpty(linkList)) {
            linkList = linkService.list(Wrappers.<Link>lambdaQuery());
            servletContext.setAttribute("linkList", linkList);
        }


        return true;
    }
}
