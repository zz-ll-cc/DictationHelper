package com.dictation.back.entity;


import java.util.Date;

/**
 * @ClassName Back
 * @Description
 * @Author zlc
 * @Date 2020-04-13 11:27
 */
public class Back {


    private int baid;
    private Date time;
    private String info;
    private int uid;
    private String type;
    private int status;
    private String path;

    public int getBaid() {
        return baid;
    }

    public void setBaid(int baid) {
        this.baid = baid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Back{" +
                "baid=" + baid +
                ", time=" + time +
                ", info='" + info + '\'' +
                ", uid=" + uid +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", path='" + path + '\'' +
                '}';
    }
}
