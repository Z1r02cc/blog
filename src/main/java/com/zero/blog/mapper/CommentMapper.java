package com.zero.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.blog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.blog.vo.CommentVo;
import org.apache.ibatis.annotations.Param;


public interface CommentMapper extends BaseMapper<Comment> {

    IPage<CommentVo> getArticleCommentList(Page<CommentVo> commentVoPage, @Param("articleId") String articleId);

}




