package com.dictation.record.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName MonthRecord
 * @Description
 * @Author lz
 * @Date 2020-04-14 21:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "tbl_month_record")
public class MonthRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer errorSum;

    private Integer rightSum;

    private Integer sum;

    private Integer userId;

    private Double accuracy;

    private String date;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
