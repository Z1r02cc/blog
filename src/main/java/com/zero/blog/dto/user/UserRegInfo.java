package com.zero.blog.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegInfo {

    @NotBlank(message = "用户名不能为空啊，铁咩")
    @Size(max = 25,min = 3,message = "用户名长度在 3-25之间")
    private String userName;

    @NotBlank(message = "密码不能为空啊，铁咩")
    @Size(max = 25,min = 3,message = "密码长度在 3-25之间")
    private String userPassword;

    @NotBlank(message = "验证码不能为空啊，铁咩")
    private String verifyCode;

    @NotBlank(message = "邮箱不能为空啊，铁咩")
    @Size(max = 35,min = 3,message = "邮箱长度在 3-35之间")
    private String userEmail;


}
