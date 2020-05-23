package com.dictation.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName ItemType
 * @Description
 * @Author zlc
 * @Date 2020-05-23 17:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("tbl_item_type")
public class ItemType {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String description;

    private Integer bookId;

    private Integer bookVersionId;

    private Integer gradeId;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Long durationTime;

}
