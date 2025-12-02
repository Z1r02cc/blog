package com.zero.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zero.blog.entity.LeaveMessage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zero.blog.vo.LeaveMessageVo;

/**
 * @author 10904
 * @description 针对表【leave_message】的数据库操作Mapper
 * @createDate 2024-04-21 19:43:25
 * @Entity com.zero.blog.entity.LeaveMessage
 */
public interface LeaveMessageMapper extends BaseMapper<LeaveMessage> {

    IPage<LeaveMessageVo> getLeaveMessageList(Page<LeaveMessageVo> leaveMessagePage);

}




