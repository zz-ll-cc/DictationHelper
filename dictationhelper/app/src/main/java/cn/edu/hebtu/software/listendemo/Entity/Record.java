package cn.edu.hebtu.software.listendemo.Entity;


/**
 * @ClassName Record
 * @Description
 * @Author zlc
 * @Date 2020-04-14 13:09
 */

public class Record {
    private Integer id;

    private Integer errorSum;

    private Integer rightSum;

    private Integer sum;

    private Integer userId;

    private Double accuracy;

    private Integer version;

    private Integer deleted;

    private String createTime;

    private String updateTime;

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", errorSum=" + errorSum +
                ", rightSum=" + rightSum +
                ", sum=" + sum +
                ", userId=" + userId +
                ", accuracy=" + accuracy +
                ", version=" + version +
                ", deleted=" + deleted +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getErrorSum() {
        return errorSum;
    }

    public void setErrorSum(Integer errorSum) {
        this.errorSum = errorSum;
    }

    public Integer getRightSum() {
        return rightSum;
    }

    public void setRightSum(Integer rightSum) {
        this.rightSum = rightSum;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
