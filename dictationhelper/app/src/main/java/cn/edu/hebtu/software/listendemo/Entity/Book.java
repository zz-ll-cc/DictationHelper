package cn.edu.hebtu.software.listendemo.Entity;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable {
    private int bid;
    private int gid;
    private int bvid;
    private String bname;
    private String bimgPath;
    private int bunitAccount;

    private int deleted;
    private int version;
    private Date createTime;
    private Date updateTime;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getBunitAccount() {
        return bunitAccount;
    }

    public void setBunitAccount(int bunitAccount) {
        this.bunitAccount = bunitAccount;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bid=" + bid +
                ", gid=" + gid +
                ", bvid=" + bvid +
                ", bname='" + bname + '\'' +
                ", bimgPath='" + bimgPath + '\'' +
                '}';
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getBvid() {
        return bvid;
    }

    public void setBvid(int bvid) {
        this.bvid = bvid;
    }

    public String getBimgPath() {
        return bimgPath;
    }

    public void setBimgPath(String bimgPath) {
        this.bimgPath = bimgPath;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }
}
