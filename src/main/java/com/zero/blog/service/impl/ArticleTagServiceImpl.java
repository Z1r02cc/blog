package com.zero.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.blog.entity.ArticleTag;
import com.zero.blog.service.ArticleTagService;
import com.zero.blog.mapper.ArticleTagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag>
        implements ArticleTagService{

    @Autowired
    ArticleTagMapper articleTagMapper;
    @Override
    public Integer ArticleTagCount() {
        Long l = articleTagMapper.selectCount(null);
        return Integer.valueOf(String.valueOf(l));
    }
}




