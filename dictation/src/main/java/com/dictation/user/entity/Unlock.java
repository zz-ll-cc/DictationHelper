package com.dictation.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName Unlock
 * @Description
 * @Author zlc
 * @Date 2020-05-13 02:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "tbl_unit_unlock_record")
public class Unlock {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer unitId;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)    //插入时填充
    private Date createTime;



}
