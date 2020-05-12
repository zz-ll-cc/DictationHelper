package com.dictation.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @ClassName User
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:46
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "tbl_user",resultMap = "userMap")
public class User {

    @TableId(type = IdType.AUTO,value = "id")
    private Integer uid;

    @TableField("user_name")
    private String uname;

    @TableField("user_city")
    private String ucity;

    @TableField("user_phone")
    private String uphone;

    @TableField("user_head_path")
    private String uheadPath;

    @TableField("user_password")
    private String upassword;

    @TableField("user_birth")
    private String ubirth;

    @TableField("is_vip")
    private Integer vip;

    @TableField("is_admin")
    private Integer is_admin;

    @TableField("user_sex")
    private String usex;

    @TableField("user_grade")
    private Integer ugrade;

    private Integer userCredit;

    private Integer accumulateStudyTime;

    private Integer accumulateStudyWords;

    private Integer accumulateSignIn;

    private Integer continuousSignIn;

    private Date lastSignInTime;

    private Date lastLoginTime;

    @Version
    private Integer version;    // 版本号

    @TableLogic
    private Integer deleted;    // 逻辑删除

    @TableField(fill = FieldFill.INSERT)    //插入时填充
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和修改时填充
    private Date updateTime;


    @TableField(exist = false)
    private List<Unlock> unlockList;


}
