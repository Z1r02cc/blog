package com.zero.blog.dto.user;

import com.zero.blog.dto.base.BasePageDto;
import lombok.Data;

@Data
public class UserListPageDto extends BasePageDto {
    private String userName;
}
