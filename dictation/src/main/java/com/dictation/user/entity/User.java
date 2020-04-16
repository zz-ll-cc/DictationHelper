package com.dictation.user.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @ClassName User
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:46
 */
public class User {
    private int uid;
    private String uname;
    private String ucity;
    private String uphone;
    private String uheadPath;
    private String upassword;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ubirth;
    private int vip;
    private int is_admin;
    private String usex;
    private int ugrade;

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUcity() {
        return ucity;
    }

    public void setUcity(String ucity) {
        this.ucity = ucity;
    }

    public String getUheadPath() {
        return uheadPath;
    }

    public void setUheadPath(String uheadPath) {
        this.uheadPath = uheadPath;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public Date getUbirth() {
        return ubirth;
    }

    public void setUbirth(Date ubirth) {
        this.ubirth = ubirth;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getIs_admin() {
        return is_admin;
    }

    public void setIs_admin(int is_admin) {
        this.is_admin = is_admin;
    }

    public String getUsex() {
        return usex;
    }

    public void setUsex(String usex) {
        this.usex = usex;
    }

    public int getUgrade() {
        return ugrade;
    }

    public void setUgrade(int ugrade) {
        this.ugrade = ugrade;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", ucity='" + ucity + '\'' +
                ", uheadPath='" + uheadPath + '\'' +
                ", upassword='" + upassword + '\'' +
                ", ubirth=" + ubirth +
                ", vip=" + vip +
                ", is_admin=" + is_admin +
                ", usex='" + usex + '\'' +
                ", ugrade=" + ugrade +
                '}';
    }
}
