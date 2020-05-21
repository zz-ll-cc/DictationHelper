package cn.edu.hebtu.software.listendemo.Entity;

import java.io.Serializable;
import java.util.Date;

public class UnLock implements Serializable {
    private int id;
    private Integer userId;
    private Integer unitId;
    private int version;
    private Integer deleted;
    private String createTime;

    @Override
    public String toString() {
        return "UnLock{" +
                "id=" + id +
                ", userId=" + userId +
                ", unitId=" + unitId +
                ", version=" + version +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
