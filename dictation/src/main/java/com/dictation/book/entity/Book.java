package com.dictation.book.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @ClassName Book
 * @Description
 * @Author zlc
 * @Date 2020-04-13 11:30
 */
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tbl_book", resultMap = "bookMap")
public class Book {

    @TableId(type = IdType.AUTO,value = "id")
    private Integer bid;

    @TableField("version_id")
    private Integer bvid;

    @TableField("book_name")
    private String bname;

    @TableField("grade_id")
    private Integer gid;

    @TableField("book_cover")
    private String bimgPath;

    private Integer bookWordVersion;

    @TableField("unit_num")
    private Integer bunitAccount;

    @Version
    private Integer version;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private List<Unit> unitList;

    
}
