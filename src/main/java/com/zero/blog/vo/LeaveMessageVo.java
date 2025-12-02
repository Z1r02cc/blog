package com.zero.blog.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class LeaveMessageVo {
    /**
     * 留言id
     */
    @TableId
    @NotBlank(message="[留言id]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("留言id")
    @Length(max= 50,message="编码长度不能超过50")
    private String leaveMessageId;
    /**
     * 留言用户id
     */
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("留言用户id")
    @Length(max= 50,message="编码长度不能超过50")
    private String userId;
    /**
     * 留言内容
     */
    @Size(max= -1,message="编码长度不能超过-1")
    @ApiModelProperty("留言内容")
    @Length(max= -1,message="编码长度不能超过-1")
    private String leaveMessageText;
    /**
     * 留言时间
     */
    @ApiModelProperty("留言时间")
    private String leaveMessageTime;


    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("留言用户的名字")
    @Length(max= 50,message="编码长度不能超过50")
    private String userName;
}
