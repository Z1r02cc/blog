package com.zero.blog.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleVo {


    /**
     * 文章id
     */
    private String articleId;


    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 文章标题
     */
    private String articleTitle;


    /**
     * 文章添加时间
     */
    private Date articleCreateTime;


    /**
     * 点赞次数
     */
    private Integer articlePraiseNum;

    /**
     * 观看次数
     */
    private Integer articleBrowseTime;


    /**
     * 文章内容
     */
    private String articleContext;


    /**
     * 文章封面
     */
    private String articleCoverUrl;


}
