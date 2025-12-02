package com.zero.blog.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 *
 * @TableName article_to_tag
 */
@Data
public class ArticleToTag implements Serializable {

    /**
     * 文章对应标签id
     */
    @TableId
    @NotBlank(message="[文章对应标签id]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("文章对应标签id")
    @Length(max= 50,message="编码长度不能超过50")
    private String articleTagListId;
    /**
     * 文章id
     */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("文章id")
    @Length(max= 50,message="编码长度不能超过50")
    private String articleId;
    /**
     * 文章标签id
     */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("文章标签id")
    @Length(max= 50,message="编码长度不能超过50")
    private String articleTagId;

}
