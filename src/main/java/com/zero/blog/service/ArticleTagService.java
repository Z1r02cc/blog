package com.zero.blog.service;

import com.zero.blog.entity.ArticleTag;
import com.baomidou.mybatisplus.extension.service.IService;


public interface ArticleTagService extends IService<ArticleTag> {

    Integer ArticleTagCount();

}
