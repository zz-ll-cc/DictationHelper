package cn.edu.hebtu.software.listendemo.Entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
    private int uid;
    private String uname;
    private String ucity;
    private String uheadPath;
    private String uphone;
    private String upassword;
    private String ubirth;
    private int vip;
    private int is_admain;
    private String usex;
    private int ugrade; //0保密,123456
    public static final int isVip = 1;
    private int deleted;
    private int version;
    private String createTime;
    private String updateTime;

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public static int getIsVip() {
        return isVip;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", ucity='" + ucity + '\'' +
                ", uheadPath='" + uheadPath + '\'' +
                ", uphone='" + uphone + '\'' +
                ", upassword='" + upassword + '\'' +
                ", ubirth='" + ubirth + '\'' +
                ", vip=" + vip +
                ", is_admain=" + is_admain +
                ", usex='" + usex + '\'' +
                ", ugrade=" + ugrade +
                '}';
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

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public String getUbirth() {
        return ubirth;
    }

    public void setUbirth(String ubirth) {
        this.ubirth = ubirth;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getIs_admain() {
        return is_admain;
    }

    public void setIs_admain(int is_admain) {
        this.is_admain = is_admain;
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
}
