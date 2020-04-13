package com.dictation.record.entity;

import java.util.Date;

/**
 * @ClassName DayRecord
 * @Description
 * @Author zlc
 * @Date 2020-04-13 15:02
 */
public class DayRecord {
    private int drid;
    private int error;
    private int uid;
    private int right;
    private int sum;
    private Date date;
    private double acc;

    public int getDrid() {
        return drid;
    }

    public void setDrid(int drid) {
        this.drid = drid;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAcc() {
        return acc;
    }

    public void setAcc(double acc) {
        this.acc = acc;
    }

    @Override
    public String toString() {
        return "DayRecord{" +
                "drid=" + drid +
                ", error=" + error +
                ", uid=" + uid +
                ", right=" + right +
                ", sum=" + sum +
                ", date=" + date +
                ", acc=" + acc +
                '}';
    }
}
