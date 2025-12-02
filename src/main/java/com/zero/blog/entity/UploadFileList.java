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
 * @TableName upload_file_list
 */
@Data
public class UploadFileList implements Serializable {

    /**
     *
     */
    @TableId
    @NotBlank(message="[]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("")
    @Length(max= 50,message="编码长度不能超过50")
    private String uploadFileListId;
    /**
     *
     */
    @ApiModelProperty("")
    private Long fileSize;
    /**
     *
     */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("")
    @Length(max= 255,message="编码长度不能超过255")
    private String fileUrl;
    /**
     *
     */
    @ApiModelProperty("")
    private Date uploadFileTime;

}
