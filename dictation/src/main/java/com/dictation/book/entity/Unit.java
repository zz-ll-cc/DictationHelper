package com.dictation.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @ClassName Unit
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tbl_unit", resultMap = "unitMap")
public class Unit {

    @TableId(type = IdType.AUTO,value = "id")
    private Integer unid;

    @TableField("unit_name")
    private String unName;

    @TableField("book_id")
    private Integer bid;

    private Integer type;

    private Integer cost;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private List<Word> wordList;



}
