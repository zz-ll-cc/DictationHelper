package cn.edu.hebtu.software.listendemo.Entity;

import java.io.Serializable;

public class Book implements Serializable {
    private int bid;
    private int gid;
    private int bvid;
    private String bname;
    private String bimgPath;
    private int bunitAccount;

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
