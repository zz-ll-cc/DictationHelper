package com.dictation.book.entity;

import java.util.Date;

/**
 * @ClassName BookVersion
 * @Description
 * @Author zlc
 * @Date 2020-04-13 14:42
 */
public class BookVersion {
    private int bvId;
    private String bvName;
    private int bvType;
    private Date bvPubTime;

    public int getBvId() {
        return bvId;
    }

    public void setBvId(int bvId) {
        this.bvId = bvId;
    }

    public String getBvName() {
        return bvName;
    }

    public void setBvName(String bvName) {
        this.bvName = bvName;
    }

    public int getBvType() {
        return bvType;
    }

    public void setBvType(int bvType) {
        this.bvType = bvType;
    }

    public Date getBvPubTime() {
        return bvPubTime;
    }

    public void setBvPubTime(Date bvPubTime) {
        this.bvPubTime = bvPubTime;
    }

    @Override
    public String toString() {
        return "BookVersion{" +
                "bvId=" + bvId +
                ", bvName='" + bvName + '\'' +
                ", bvType=" + bvType +
                ", bvPubTime=" + bvPubTime +
                '}';
    }
}
