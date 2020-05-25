package cn.edu.hebtu.software.listendemo.Entity;

import java.util.Date;

/**
 * @ClassName Inventory
 * @Description 用户库存
 * @Author zlc
 * @Date 2020-05-22 18:40
 */

import java.util.Date;

public class Inventory {

    private Integer id;

    private String name;

    private String approachOfAchieving;//获取途径（积分兑换，系统赠送）

    private Integer userId;

    private Integer itemId;

    private Integer isUsed;//是否使用了,0未使用，1使用过了

    private String expendTime;//什么时候使用的

    private Integer deleted;//逻辑删除

    private String purchaseTime;//购买时间

    private String expiryTime;//到期时间

    private Item item;//这个库存的name应该由他所对应的item的type来决定

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

    public String getApproachOfAchieving() {
        return approachOfAchieving;
    }

    public void setApproachOfAchieving(String approachOfAchieving) {
        this.approachOfAchieving = approachOfAchieving;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }


    public String getExpendTime() {
        return expendTime;
    }

    public void setExpendTime(String expendTime) {
        this.expendTime = expendTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(String purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", approachOfAchieving='" + approachOfAchieving + '\'' +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", isUsed=" + isUsed +
                ", expendTime='" + expendTime + '\'' +
                ", deleted=" + deleted +
                ", purchaseTime='" + purchaseTime + '\'' +
                ", expiryTime='" + expiryTime + '\'' +
                ", item=" + item +
                '}';
    }

    public Inventory(){

    }
}
