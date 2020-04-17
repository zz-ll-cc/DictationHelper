package com.dictation.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.jnlp.IntegrationService;
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

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String wordEnglish;

    private String wordChinese;

    private Integer unitId;

    private Integer bookId;

    private Integer type;

    private String wordImg;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
