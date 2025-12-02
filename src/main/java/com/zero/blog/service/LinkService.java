package com.zero.blog.service;

import com.zero.blog.entity.Link;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface LinkService extends IService<Link> {


    List<Link> getLink();

}
