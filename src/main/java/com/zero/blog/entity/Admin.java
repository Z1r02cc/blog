package com.zero.blog.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @TableName admin
 */
@Data
public class Admin implements Serializable {

    /**
     * 管理员id
     */
    @TableId
    @NotBlank(message="[管理员id]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("管理员id")
    @Length(max= 50,message="编码长度不能超过50")
    private String admin;
    /**
     * 管理员名称
     */
    @Size(max= 35,message="编码长度不能超过35")
    @ApiModelProperty("管理员名称")
    @Length(max= 35,message="编码长度不能超过35")
    private String adminName;
    /**
     * 管理员密码
     */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("管理员密码")
    @Length(max= 50,message="编码长度不能超过50")
    private String adminPassword;

}
