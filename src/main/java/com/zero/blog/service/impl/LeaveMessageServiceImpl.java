package com.zero.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zero.blog.entity.LeaveMessage;
import com.zero.blog.service.LeaveMessageService;
import com.zero.blog.mapper.LeaveMessageMapper;
import com.zero.blog.vo.LeaveMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LeaveMessageServiceImpl extends ServiceImpl<LeaveMessageMapper, LeaveMessage>
        implements LeaveMessageService{

    @Autowired
    LeaveMessageMapper leaveMessageMapper;
    @Override
    public IPage<LeaveMessageVo> getLeaveMessageList(Page<LeaveMessageVo> leaveMessagePage) {
        return leaveMessageMapper.getLeaveMessageList(leaveMessagePage);
    }
}




