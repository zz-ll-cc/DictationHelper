package com.dictation.record.entity;

import java.util.Date;

/**
 * @ClassName Record
 * @Description
 * @Author zlc
 * @Date 2020-04-14 13:09
 */
public class Record {

    private int rid;
    private int error;
    private int rright;
    private Date date;
    private int uid;
    private int sum;
    private double acc;

    public Record(int error, int rright, Date date, int uid, int sum, double acc) {
        this.error = error;
        this.rright = rright;
        this.date = date;
        this.uid = uid;
        this.sum = sum;
        this.acc = acc;
    }
    public Record(){}

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getRright() {
        return rright;
    }

    public void setRright(int rright) {
        this.rright = rright;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public double getAcc() {
        return acc;
    }

    public void setAcc(double acc) {
        this.acc = acc;
    }

    @Override
    public String toString() {
        return "Record{" +
                "rid=" + rid +
                ", error=" + error +
                ", rright=" + rright +
                ", date=" + date +
                ", uid=" + uid +
                ", sum=" + sum +
                ", acc=" + acc +
                '}';
    }
}