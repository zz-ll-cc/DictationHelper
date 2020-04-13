package com.dictation.book.entity;

/**
 * @ClassName Unit
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:17
 */
public class Unit {

    private int unid;
    private String unName;
    private int bid;
    private int type;

    public int getUnid() {
        return unid;
    }

    public void setUnid(int unid) {
        this.unid = unid;
    }

    public String getUnName() {
        return unName;
    }

    public void setUnName(String unName) {
        this.unName = unName;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "unid=" + unid +
                ", unName='" + unName + '\'' +
                ", bid=" + bid +
                ", type=" + type +
                '}';
    }
}
