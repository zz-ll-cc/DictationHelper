package com.dictation.record.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.jnlp.IntegrationService;
import java.util.Date;

/**
 * @ClassName Record
 * @Description
 * @Author zlc
 * @Date 2020-04-14 13:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "tbl_record")
public class Record {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer errorSum;

    private Integer rightSum;

    private Integer sum;

    private Integer userId;

    private Double accuracy;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;



}
