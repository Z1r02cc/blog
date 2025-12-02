package com.zero.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.blog.entity.Comment;
import com.zero.blog.service.CommentService;
import com.zero.blog.mapper.CommentMapper;
import com.zero.blog.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
        implements CommentService{

    @Autowired
    CommentMapper commentMapper;

    @Override
    public IPage<CommentVo> getArticleCommentList(Page<CommentVo> commentVoPage, String articleId) {
        return commentMapper.getArticleCommentList(commentVoPage,articleId);
    }
}




