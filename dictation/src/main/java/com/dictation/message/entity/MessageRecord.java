package com.dictation.message.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName MessageRecord
 * @Description
 * @Author zlc
 * @Date 2020-05-21 18:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tbl_message_record")
public class MessageRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer messageId;

    private Integer hits;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;



}
