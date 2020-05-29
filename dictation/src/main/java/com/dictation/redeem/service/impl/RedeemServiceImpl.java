package com.dictation.redeem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dictation.annotations.Time;
import com.dictation.mall.entity.Inventory;
import com.dictation.mall.service.InventoryService;
import com.dictation.mapper.RedeemMapper;
import com.dictation.redeem.entity.Redeem;
import com.dictation.redeem.service.RedeemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName RedeemServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-05-27 16:38
 */
@Service("redeemService")
public class RedeemServiceImpl implements RedeemService {

    @Autowired
    RedeemMapper redeemMapper;

    @Autowired
    InventoryService inventoryService;

    @Override
    @Time
    public int insertBatch(List<Redeem> redeems) {
        return redeemMapper.insertBatch(redeems);
    }

    @Override
    public int findUnusedByTypeId(Integer typeId) {
        return redeemMapper.selectCount(new QueryWrapper<Redeem>().eq("redeem_type_id",typeId).eq("is_used",0));
    }

    @Override
    public int updateOne(Redeem redeem) {
        return redeemMapper.updateById(redeem);
    }

    @Override
    @Time
    public int updateBatch(Integer typeId) {
        return redeemMapper.update(new Redeem().setIsUsed(1), new QueryWrapper<Redeem>().eq("redeem_type_id", typeId));
    }

    @Override
    public int updateBatch(Date date) {
        return redeemMapper.update(new Redeem().setIsUsed(1), new QueryWrapper<Redeem>().lt("create_time", date));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean useOne(String redeemString,Integer userId) {
        Redeem redeem = redeemMapper.selectOne(new QueryWrapper<Redeem>().eq("redeem_string",redeemString));
        if(redeem.getIsUsed() == 1) return false;
        redeemMapper.updateById(redeem.setIsUsed(1));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        //商品的有效期都是7天
        calendar.add(Calendar.HOUR, 24*7);
        inventoryService.addInventory(new Inventory()
                .setApproachOfAchieving("CDKEY兑换")
                .setItemId(1)
                .setExpiryTime(calendar.getTime())
                .setUserId(userId)
                .setName("畅读券"));

        return true;
    }

    @Override
    public String getOneByTypeId(Integer typeId) {
        List<Redeem> redeems = redeemMapper.selectList(new QueryWrapper<Redeem>().eq("is_used", 0).eq("redeem_type_id", typeId));
        if(redeems == null || redeems.size() == 0) return null;
        return redeems.get(0).getRedeemString();
    }


}
