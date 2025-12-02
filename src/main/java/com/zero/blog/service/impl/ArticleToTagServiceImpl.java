package com.zero.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.blog.entity.ArticleToTag;
import com.zero.blog.service.ArticleToTagService;
import com.zero.blog.mapper.ArticleToTagMapper;
import org.springframework.stereotype.Service;

@Service
public class ArticleToTagServiceImpl extends ServiceImpl<ArticleToTagMapper, ArticleToTag>
        implements ArticleToTagService{

}




