package cn.edu.hebtu.software.listendemo.Entity;

import java.io.Serializable;
import java.util.Date;

public class Word implements Serializable {
    public static final int TYPE_KEYNODE = 1;
    public static final int TYPE_UN_KEYNODE = 0;
    private int unid;
    private int bid;
    private int wid;
    private int type;
    private String wimgPath;
    private String wchinese;
    private String wenglish;
    private int isTrue;
    private int deleted;
    private int version;
    private Date createTime;
    private Date updateTime;

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Word{" +
                "unid=" + unid +
                ", bid=" + bid +
                ", wid=" + wid +
                ", type=" + type +
                ", wimgPath='" + wimgPath + '\'' +
                ", wchinese='" + wchinese + '\'' +
                ", wenglish='" + wenglish + '\'' +
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

}
