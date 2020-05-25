package cn.edu.hebtu.software.listendemo.Entity;


public class Item {

    private Integer id;

    private String name;//商品名称

    private String cover;//商品封面

    private String description;//商品描述

    private Integer quantity;//商品剩余数量

    private Integer itemTypeId;//商品类型，voucher

    private Integer price;//商品价格

    private Integer version;//乐观锁

    private Integer deleted;//逻辑删除

    private String createTime;

    private String updateTime;

    private ItemType itemType;

    private int left;

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }



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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(Integer itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
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

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cover='" + cover + '\'' +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", itemTypeId=" + itemTypeId +
                ", price=" + price +
                ", version=" + version +
                ", deleted=" + deleted +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", itemType=" + itemType +
                ", left=" + left +
                '}';
    }
}
