package com.zero.blog.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @TableName link
 */
@Data
public class Link implements Serializable {

    /**
     * 链接的id
     */
    @TableId
    @NotBlank(message="[链接的id]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("链接的id")
    @Length(max= 50,message="编码长度不能超过50")
    private String linkId;
    /**
     * 链接标题
     */
    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("链接标题")
    @Length(max= 100,message="编码长度不能超过100")
    private String linkTitle;
    /**
     * 链接url
     */
    @Size(max= 500,message="编码长度不能超过500")
    @ApiModelProperty("链接url")
    @Length(max= 500,message="编码长度不能超过500")
    private String linkUrl;
    /**
     * 链接logo
     */
    @Size(max= 500,message="编码长度不能超过500")
    @ApiModelProperty("链接logo")
    @Length(max= 500,message="编码长度不能超过500")
    private String linkLogoUrl;
    /**
     * 创建链接时间
     */
    @ApiModelProperty("创建链接时间")
    private Date linkCreateTime;

}
