package com.zero.blog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zero.blog.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;


public interface UserService extends IService<User> {
    Integer UserCount();
    IPage<User> selectPage(IPage<User> page, LambdaQueryWrapper<User> lambdaQueryWrapper);
    int delUser(String userId);
}
