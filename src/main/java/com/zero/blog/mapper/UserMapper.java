package com.zero.blog.mapper;

import com.zero.blog.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.io.Serializable;

/**
 * @author 10904
 * @description 针对表【user】的数据库操作Mapper
 * @createDate 2024-02-02 01:01:05
 * @Entity com.zero.blog.entity.User
 */
public interface UserMapper extends BaseMapper<User> {
    @Override
    User selectById(Serializable id);
}




