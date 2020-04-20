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
    private String createTime;
    private String updateTime;

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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
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
