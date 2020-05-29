package com.dictation.redeem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName RedeemType
 * @Description
 * @Author zlc
 * @Date 2020-05-27 16:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("tbl_redeem_type")
public class RedeemType {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String typeName;

    private Integer total;

    private String description;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
