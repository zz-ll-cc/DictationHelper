package com.dictation.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName Book
 * @Description
 * @Author zlc
 * @Date 2020-04-13 11:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tbl_book")
public class Book {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer versionId;

    private String bookName;

    private Integer gradeId;

    private String bookCover;

    private Integer unitNum;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
