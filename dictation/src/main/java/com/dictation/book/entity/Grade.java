package com.dictation.book.entity;

/**
 * @ClassName Grade
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:11
 */
public class Grade {

    private int gid;
    private String gname;
    private int gtype;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public int getGtype() {
        return gtype;
    }

    public void setGtype(int gtype) {
        this.gtype = gtype;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gid=" + gid +
                ", gname='" + gname + '\'' +
                ", gtype=" + gtype +
                '}';
    }
}
