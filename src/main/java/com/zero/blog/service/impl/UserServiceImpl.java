package com.zero.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.blog.entity.User;
import com.zero.blog.service.UserService;
import com.zero.blog.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService{

    @Autowired
    UserMapper userMapper;

    @Override
    public Integer UserCount() {
        Long l = userMapper.selectCount(null);
        return Integer.valueOf(String.valueOf(l));
    }

    @Override
    public IPage<User> selectPage(IPage<User> page, LambdaQueryWrapper<User> lambdaQueryWrapper) {
        IPage<User> userIPage = userMapper.selectPage(page, lambdaQueryWrapper);
        return userIPage;
    }

    @Override
    public int delUser(String userId) {
        return userMapper.deleteById(userId);
    }


}




