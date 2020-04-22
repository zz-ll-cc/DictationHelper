package com.dictation.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * @ClassName Word
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "tbl_word")
public class Word {

    @TableId(type = IdType.AUTO,value = "id")
    private Integer wid;

    @TableField("word_english")
    private String wenglish;

    @TableField("word_chinese")
    private String wchinese;

    @TableField("unit_id")
    private Integer unid;

    @TableField("book_id")
    private Integer bid;

    private Integer type;

    @TableField("word_img")
    private String wimgPath;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
