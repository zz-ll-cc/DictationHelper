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
    private int dright;
    private int sum;
    private Date date;
    private double acc;

    public DayRecord(int error, int uid, int dright, int sum, Date date, double acc) {
        this.drid = drid;
        this.error = error;
        this.uid = uid;
        this.dright = dright;
        this.sum = sum;
        this.date = date;
        this.acc = acc;
    }

    public DayRecord(){}

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

    public int getDright() {
        return dright;
    }

    public void setDright(int dright) {
        this.dright = dright;
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
                ", dright=" + dright +
                ", sum=" + sum +
                ", date=" + date +
                ", acc=" + acc +
                '}';
    }
}
