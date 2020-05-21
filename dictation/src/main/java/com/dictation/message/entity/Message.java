package com.dictation.message.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName Message
 * @Description
 * @Author zlc
 * @Date 2020-05-21 15:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tbl_message")
public class Message {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String titleImage;

    private String title;

    private String subtitle;

    private String content;

    private String typeName;

    private Integer hits;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
