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
 * @TableName comment
 */
@Data
public class Comment implements Serializable {

    /**
     * 文章评论id
     */
    @TableId
    @NotBlank(message="[文章评论id]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("文章评论id")
    @Length(max= 50,message="编码长度不能超过50")
    private String commentId;
    /**
     * 文章id
     */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("文章id")
    @Length(max= 50,message="编码长度不能超过50")
    private String articleId;
    /**
     * 评论时间
     */
    @ApiModelProperty("评论时间")
    private Date commentTime;
    /**
     * 评论用户id
     */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("评论用户id")
    @Length(max= 50,message="编码长度不能超过50")
    private String commentUser;
    /**
     * 评论内容
     */
    @Size(max= -1,message="编码长度不能超过-1")
    @ApiModelProperty("评论内容")
    @Length(max= -1,message="编码长度不能超过-1")
    private String commentContent;

}
