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
 * @TableName article
 */
@Data
public class Article implements Serializable {

    /**
     * 文章id
     */
    @TableId
    @NotBlank(message="[文章id]不能为空")
    @Size(max= 52,message="编码长度不能超过52")
    @ApiModelProperty("文章id")
    @Length(max= 52,message="编码长度不能超过52")
    private String articleId;
    /**
     * 作者id
     */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("作者id")
    @Length(max= 50,message="编码长度不能超过50")
    private String userId;
    /**
     * 文章标题
     */
    @Size(max= 520,message="编码长度不能超过520")
    @ApiModelProperty("文章标题")
    @Length(max= 520,message="编码长度不能超过520")
    private String articleTitle;
    /**
     * 文章创建时间
     */
    @ApiModelProperty("文章创建时间")
    private Date articleCreateTime;
    /**
     * 文章内容
     */
    @Size(max= -1,message="编码长度不能超过-1")
    @ApiModelProperty("文章内容")
    @Length(max= -1,message="编码长度不能超过-1")
    private String articleContext;
    /**
     * 点赞次数
     */
    @ApiModelProperty("点赞次数")
    private Integer articlePraiseNum;
    /**
     * 浏览次数
     */
    @ApiModelProperty("浏览次数")
    private Integer articleBrowseTime;

    private String articleCoverUrl;
}
