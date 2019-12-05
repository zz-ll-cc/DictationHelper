package cn.edu.hebtu.software.listendemo.Entity;

import java.util.Date;

public class WrongWord {
    public static final int TYPE_KEYNODE = 1;
    public static final int TYPE_UN_KEYNODE = 0;
    private int wid;
    private String wimgPath;
    private String wchinese;
    private String wenglish;
    private int unid;
    private int bid;
    private int type;
    private Date time;
    private int isTrue;

    @Override
    public String toString() {
        return "WrongWord{" +
                "wid=" + wid +
                ", wimgPath='" + wimgPath + '\'' +
                ", wchinese='" + wchinese + '\'' +
                ", wenglish='" + wenglish + '\'' +
                ", unid=" + unid +
                ", bid=" + bid +
                ", type=" + type +
                ", time=" + time +
                ", isTrue=" + isTrue +
                '}';
    }

    public int getUnid() {
        return unid;
    }

    public void setUnid(int unid) {
        this.unid = unid;
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

    public String getWimgPath() {
        return wimgPath;
    }

    public void setWimgPath(String wimgPath) {
        this.wimgPath = wimgPath;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public String getWchinese() {
        return wchinese;
    }

    public void setWchinese(String wchinese) {
        this.wchinese = wchinese;
    }

    public String getWenglish() {
        return wenglish;
    }

    public void setWenglish(String wenglish) {
        this.wenglish = wenglish;
    }

    public int getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(int isTrue) {
        this.isTrue = isTrue;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
