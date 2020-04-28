package cn.edu.hebtu.software.listendemo.Entity;

/**
 * @ClassName MonthRecord
 * @Description
 * @Author szy
 * @Date 2020-04-28 21:46
 */
public class MonthRecord {
    private Integer id;

    private Integer errorSum;

    private Integer rightSum;

    private Integer sum;

    private Integer userId;

    private Double accuracy;

    private String date;

    private Integer version;

    private Integer deleted;

    private String createTime;

    private String updateTime;

    @Override
    public String toString() {
        return "MonthRecord{" +
                "id=" + id +
                ", errorSum=" + errorSum +
                ", rightSum=" + rightSum +
                ", sum=" + sum +
                ", userId=" + userId +
                ", accuracy=" + accuracy +
                ", date='" + date + '\'' +
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
