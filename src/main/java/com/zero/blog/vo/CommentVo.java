package com.zero.blog.vo;

import lombok.Data;

@Data
public class CommentVo {
    /**
     * 文章评论id
     */
    private String commentId;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 用户id（评论人）
     */
    private String comment_user;

    /**
     * 文章评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    private String commentTime;


    /**
     * 用户名称
     */
    private String userName;


}
