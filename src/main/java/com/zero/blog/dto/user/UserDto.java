package com.zero.blog.dto.user;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
@Data
public class UserDto {
    /**
     * 用户ID
     */
    @TableId
    @NotBlank(message="[用户ID]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("用户ID")
    @Length(max= 50,message="编码长度不能超过50")
    private String userId;
    /**
     * 用户名
     */
    @Size(max= 35,message="编码长度不能超过35")
    @ApiModelProperty("用户名")
    @Length(max= 35,message="编码长度不能超过35")
    private String userName;
    /**
     * 用户密码
     */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("用户密码")
    @Length(max= 50,message="编码长度不能超过50")
    private String userPassword;

    /**
     * 用户邮箱
     */
    @Size(max= 60,message="编码长度不能超过60")
    @ApiModelProperty("用户邮箱")
    @Length(max= 60,message="编码长度不能超过60")
    private String userEmail;

    /**
     * 用户冻结 0无冻结 1冻结
     */
    private int userFreezing;
}
