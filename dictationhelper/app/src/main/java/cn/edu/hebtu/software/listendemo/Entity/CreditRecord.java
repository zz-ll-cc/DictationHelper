package cn.edu.hebtu.software.listendemo.Entity;

import java.util.Date;

public class CreditRecord {

    private Integer id;

    private Integer userId;

    private String reason;

    private Integer increment;

    private Integer version;

    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getIncrement() {
        return increment;
    }

    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
