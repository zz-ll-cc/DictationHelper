package cn.edu.hebtu.software.listendemo.Entity;

import java.io.Serializable;

public class Version implements Serializable {
    private int bvId;
    private String bvName;
    private String bvPubTime;
    private int nvType;

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
