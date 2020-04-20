package cn.edu.hebtu.software.listendemo.Entity;

import java.io.Serializable;
import java.util.Date;

public class Version implements Serializable {
    private int bvId;
    private String bvName;
    private String bvPubTime;
    private int nvType;
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

    public String getBvPubTime() {
        return bvPubTime;
    }

    public void setBvPubTime(String bvPubTime) {
        this.bvPubTime = bvPubTime;
    }

    public int getNvType() {
        return nvType;
    }

    public void setNvType(int nvType) {
        this.nvType = nvType;
    }
}
