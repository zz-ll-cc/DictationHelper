package com.dictation.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.jnlp.IntegrationService;
import java.util.Date;

/**
 * @ClassName Grade
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tbl_grade")
public class Grade {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String gradeName;

    private Integer gradeType;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}
