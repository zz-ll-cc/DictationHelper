package com.dictation.book.entity;

/**
 * @ClassName Word
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:24
 */
public class Word {

    private int wid;
    private String wenglish;
    private String wchinese;
    private int unId;
    private int bid;
    private int type;
    private String wimgPath;

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public String getWenglish() {
        return wenglish;
    }

    public void setWenglish(String wenglish) {
        this.wenglish = wenglish;
    }

    public String getWchinese() {
        return wchinese;
    }

    public void setWchinese(String wchinese) {
        this.wchinese = wchinese;
    }

    public int getUnId() {
        return unId;
    }

    public void setUnId(int unId) {
        this.unId = unId;
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

    @Override
    public String toString() {
        return "Word{" +
                "wid=" + wid +
                ", wenglish='" + wenglish + '\'' +
                ", wchinese='" + wchinese + '\'' +
                ", unId=" + unId +
                ", bid=" + bid +
                ", type=" + type +
                ", wimgPath='" + wimgPath + '\'' +
                '}';
    }
}
