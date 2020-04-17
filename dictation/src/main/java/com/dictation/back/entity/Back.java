package com.dictation.back.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName Back
 * @Description
 * @Author zlc
 * @Date 2020-04-13 11:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tbl_back")
public class Back {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String info;

    private Integer uid;

    private String type;

    @TableLogic
    private Integer deleted;

    @Version
    private Integer version;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
