package com.zero.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.blog.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.blog.vo.CommentVo;


public interface CommentService extends IService<Comment> {
    IPage<CommentVo> getArticleCommentList(Page<CommentVo> commentVoPage, String articleId);
}
