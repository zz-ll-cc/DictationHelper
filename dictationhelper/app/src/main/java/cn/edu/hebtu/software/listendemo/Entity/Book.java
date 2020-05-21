package cn.edu.hebtu.software.listendemo.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Book implements Serializable {
    private int bid;
    private int gid;
    private int bvid;
    private String bname;
    private String bimgPath;
    private int bunitAccount;
    private int bookWordVersion;

    private List<Unit> units = new ArrayList<>();


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

    public int getBunitAccount() {
        return bunitAccount;
    }

    public void setBunitAccount(int bunitAccount) {
        this.bunitAccount = bunitAccount;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public int getBookWordVersion() {
        return bookWordVersion;
    }

    public void setBookWordVersion(int bookWordVersion) {
        this.bookWordVersion = bookWordVersion;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bid=" + bid +
                ", gid=" + gid +
                ", bvid=" + bvid +
                ", bname='" + bname + '\'' +
                ", bimgPath='" + bimgPath + '\'' +
                ", bunitAccount=" + bunitAccount +
                ", bookWordVersion=" + bookWordVersion +
                ", units=" + units +
                ", deleted=" + deleted +
                ", version=" + version +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
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
