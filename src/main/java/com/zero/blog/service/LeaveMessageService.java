package com.zero.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.blog.entity.LeaveMessage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zero.blog.vo.LeaveMessageVo;


public interface LeaveMessageService extends IService<LeaveMessage> {

    IPage<LeaveMessageVo> getLeaveMessageList(Page<LeaveMessageVo> leaveMessagePage);


}
