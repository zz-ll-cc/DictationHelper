package com.dictation.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @ClassName Inventory
 * @Description 用户库存
 * @Author zlc
 * @Date 2020-05-22 18:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName(value = "tbl_inventory",resultMap = "inventoryMap")
public class Inventory {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String approachOfAchieving;//获取途径（积分兑换，系统赠送）

    private Integer userId;

    private Integer itemId;

    private Integer isUsed;//是否使用了,0未使用，1使用过了

    private Date expendTime;//什么时候使用的

    @TableLogic
    private Integer deleted;//逻辑删除

    @TableField(fill = FieldFill.INSERT)
    private Date purchaseTime;//购买时间

    private Date expiryTime;//到期时间

    @TableField(exist = false)
    private Item item;//这个库存的name应该由他所对应的item的type来决定



}
