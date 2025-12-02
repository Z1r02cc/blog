package com.zero.blog.dto.base;

import lombok.Data;

@Data
public class BasePageDto {
    /**
     * 当前的页数
     */
    private Integer pageNum = 1;
    /**
     * 一页显示多少条数据
     */
    private Integer pageSize = 10;
}
