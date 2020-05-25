package cn.edu.hebtu.software.listendemo.Entity;


import java.util.Date;

/**
 * @ClassName ItemType
 * @Description
 * @Author zlc
 * @Date 2020-05-23 17:45
 */
public class ItemType {

    private Integer id;

    private String name;

    private String description;

    private Integer bookId;

    private Integer bookVersionId;

    private Integer gradeId;

    private Integer version;

    private Integer deleted;

    private String createTime;

    private Long durationTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getBookVersionId() {
        return bookVersionId;
    }

    public void setBookVersionId(Integer bookVersionId) {
        this.bookVersionId = bookVersionId;
    }

    public Integer getGradeId() {
        return gradeId;
    }

    public void setGradeId(Integer gradeId) {
        this.gradeId = gradeId;
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

    public Long getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(Long durationTime) {
        this.durationTime = durationTime;
    }

    @Override
    public String toString() {
        return "ItemType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", bookId=" + bookId +
                ", bookVersionId=" + bookVersionId +
                ", gradeId=" + gradeId +
                ", version=" + version +
                ", deleted=" + deleted +
                ", createTime='" + createTime + '\'' +
                ", durationTime=" + durationTime +
                '}';
    }
}
