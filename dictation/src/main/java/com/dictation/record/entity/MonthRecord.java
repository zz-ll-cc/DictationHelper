package com.dictation.record.entity;

import java.util.Date;

/**
 * @ClassName MonthRecord
 * @Description
 * @Author liuzhe
 * @Date 2020-04-14 21:46
 */
public class MonthRecord {
    private int mrid;
    private int error;
    private int uid;
    private int mright;
    private int sum;
    private String date;
    private double acc;

    public MonthRecord( int error, int uid, int mright, int sum, String date, double acc) {
        this.mrid = mrid;
        this.error = error;
        this.uid = uid;
        this.mright = mright;
        this.sum = sum;
        this.date = date;
        this.acc = acc;
    }

    public MonthRecord() { }

    public int getMrid() {
        return mrid;
    }

    public void setMrid(int mrid) {
        this.mrid = mrid;
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

    public int getMright() {
        return mright;
    }

    public void setMright(int mright) {
        this.mright = mright;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
        return "MonthRecord{" +
                "mrid=" + mrid +
                ", error=" + error +
                ", uid=" + uid +
                ", mright=" + mright +
                ", sum=" + sum +
                ", date='" + date + '\'' +
                ", acc=" + acc +
                '}';
    }
}
