package com.dictation.book.entity;

/**
 * @ClassName Book
 * @Description
 * @Author zlc
 * @Date 2020-04-13 11:30
 */
public class Book {

    private int bid;
    private int bvid;
    private String bname;
    private int gid;
    private String bimgPath;
    private int bunitAccount;

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getBvid() {
        return bvid;
    }

    public void setBvid(int bvid) {
        this.bvid = bvid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getBimgPath() {
        return bimgPath;
    }

    public void setBimgPath(String bimgPath) {
        this.bimgPath = bimgPath;
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
                ", bvid=" + bvid +
                ", bname='" + bname + '\'' +
                ", gid=" + gid +
                ", bimgPath='" + bimgPath + '\'' +
                ", bunitAccount=" + bunitAccount +
                '}';
    }
}
