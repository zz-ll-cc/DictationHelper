package com.dictation.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * @ClassName Item
 * @Description
 * @Author zlc
 * @Date 2020-05-22 17:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "tbl_item",resultMap = "itemMap")
public class Item {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;//商品名称

    private String cover;//商品封面

    private String description;//商品描述

    private Integer quantity;//商品剩余数量

    private Integer itemTypeId;//商品类型，voucher

    private Integer price;//商品价格

    @Version
    private Integer version;//乐观锁

    @TableLogic
    private Integer deleted;//逻辑删除

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //多对一映射
    @TableField(exist = false)
    private ItemType itemType;


}
