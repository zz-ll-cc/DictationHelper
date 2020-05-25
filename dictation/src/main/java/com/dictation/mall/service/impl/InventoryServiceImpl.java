package com.dictation.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.mall.entity.Inventory;
import com.dictation.mall.service.InventoryService;
import com.dictation.mapper.InventoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @ClassName InventoryServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-05-23 16:31
 */
@Service("inventoryService")
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    InventoryMapper inventoryMapper;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean addInventory(Inventory inventory) {
        if(inventoryMapper.insert(inventory) == 1){
            return true;
        }
        logger.error("添加用户库存到数据库失败");
        return false;
    }

    @Override
    public boolean deleteInventory(Integer id) {

        if(inventoryMapper.deleteById(id) == 1){
            return true;
        }
        logger.error("数据库删除用户库存失败");
        return false;
    }

    @Override
    public boolean updateInventory(Inventory inventory) {
        if(inventoryMapper.updateById(inventory) == 1){
            return true;
        }
        logger.error("更新用户库存失败");
        return false;
    }

    @Override
    public Inventory findOneById(Integer id) {
        return inventoryMapper.selectById(id);
    }

    @Override
    public List<Inventory> findAll() {
        return inventoryMapper.selectList(null);
    }

    @Override
    public List<Inventory> findAll(Integer pageSize, Integer pageNum) {
        Page<Inventory> page = new Page<>(pageNum,pageSize);
        return inventoryMapper.selectPage(page,null).getRecords();
    }

    @Override
    public List<Inventory> findAllByUserId(Integer userId, Integer pageSize, Integer pageNum) {
        QueryWrapper<Inventory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        Page<Inventory> page = new Page<>(pageNum,pageSize);
        return inventoryMapper.selectPage(page,wrapper).getRecords();
    }

    @Override
    public List<Inventory> findAllByUserId(Integer userId) {
        QueryWrapper<Inventory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        return inventoryMapper.selectList(wrapper);
    }

    @Override
    public List<Inventory> findAllByUserIdOrderByExpiryTimeAsc(Integer userId, Integer pageSize, Integer pageNum) {
        QueryWrapper<Inventory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId).gt("expiry_time",new Date()).orderByAsc("expiry_time");
        Page<Inventory> page = new Page<>(pageNum,pageSize);
        return inventoryMapper.selectPage(page,wrapper).getRecords();
    }

    @Override
    public List<Inventory> findAllByUserIdOrderByExpiryTimeAsc(Integer userId) {
        QueryWrapper<Inventory> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId).gt("expiry_time",new Date()).orderByAsc("expiry_time");
        return inventoryMapper.selectList(wrapper);
    }



    @Override
    public Inventory useInventory(Integer id,Integer userId) {
        Inventory inventory = inventoryMapper.selectById(id);
        Date date = new Date();
        if(inventory.getUserId() != userId){
            logger.error("用户库存与用户id不匹配，传入参数为：" + id + ",userId" + userId + "，时间：" + date);
            return null;
        }
        inventory.setIsUsed(1).setExpendTime(date);
        if(inventoryMapper.updateById(inventory) == 1){
            return inventory;
        }
        logger.error("用户库存使用失败，传入参数为：" + id + ",userId" + userId + "，时间：" + date);
        return null;
    }


}
