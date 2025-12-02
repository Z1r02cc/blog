package com.zero.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.blog.entity.Link;
import com.zero.blog.service.LinkService;
import com.zero.blog.mapper.LinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link>
        implements LinkService{
    @Autowired
    LinkMapper linkMapper;


    @Override
    public List<Link> getLink() {
        return null;
    }
}




