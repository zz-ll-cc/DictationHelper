package com.dictation.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName User
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tbl_user")
public class User {
    @TableId(type = IdType.AUTO)
    private int uid;
    private String uname;
    private String ucity;
    private String uphone;
    @TableField("uheadPath")
    private String uheadPath;
    private String upassword;
    private String ubirth;
    private int vip;
    @TableField("is_admin")
    private int is_admin;
    private String usex;
    private int ugrade;

    @Version
    private Integer version;    // 版本号

    @TableLogic
    private Integer deleted;    // 逻辑删除

    @TableField(fill = FieldFill.INSERT)    //插入时填充
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和修改时填充
    private Date updateTime;


}
