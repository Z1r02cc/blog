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
 * @TableName article_tag
 */
@Data
public class ArticleTag implements Serializable {

    /**
     * 文章标签id
     */
    @TableId
    @NotBlank(message="[文章标签id]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("文章标签id")
    @Length(max= 50,message="编码长度不能超过50")
    private String articleTagId;
    /**
     * 标签名称
     */
    @Size(max= 35,message="编码长度不能超过35")
    @ApiModelProperty("标签名称")
    @Length(max= 35,message="编码长度不能超过35")
    private String articleTagName;
    /**
     * 添加时间
     */
    @ApiModelProperty("添加时间")
    private Date articleTagAddTime;

}
