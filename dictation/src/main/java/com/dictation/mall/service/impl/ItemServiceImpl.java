package com.dictation.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.mall.entity.Inventory;
import com.dictation.mall.entity.Item;
import com.dictation.mall.entity.ItemType;
import com.dictation.mall.service.InventoryService;
import com.dictation.mall.service.ItemService;
import com.dictation.mapper.ItemMapper;
import com.dictation.user.entity.User;
import com.dictation.user.service.UserService;
import com.dictation.util.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ItemServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-05-23 00:31
 */
@Service("itemService")
public class ItemServiceImpl implements ItemService {

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    UserService userService;


    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public boolean addItem(Item item) {
        if(itemMapper.insert(item) == 1){
            return true;
        }
        logger.error("添加商品到数据库失败");
        return false;
    }

    @Override
    public boolean deleteItem(Integer id) {
        if(itemMapper.deleteById(id) == 1){
            return true;
        }
        logger.error("数据库删除商品失败");
        return false;
    }

    @Override
    public boolean deleteItem(Item item) {
        if(itemMapper.deleteById(item.getId()) == 1){
            return true;
        }
        logger.error("数据库删除商品失败");
        return false;
    }

    /**
     * 禁止更新item的itemType！！！！！！可能会出现数据库的数量与缓存数量不一致的情况
     * @param item
     * @return
     */
    @Override
    public boolean updateItem(Item item) {
        if(itemMapper.updateById(item) == 1){
            return true;
        }
        logger.error("数据库更新商品失败");
        return false;
    }

    @Override
    public Item findOneById(Integer id) {
        return itemMapper.selectById(id);
    }

    @Override
    public List<Item> findAllItem() {
        return itemMapper.selectList(null);
    }

    @Override
    public List<Item> findAllItemOrderByCreateTimeDescAndPaging(Integer pageSize, Integer pageNum) {
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        Page<Item> page = new Page<>(pageNum,pageSize);
        return itemMapper.selectPage(page,queryWrapper).getRecords();
    }

    @Override
    public List<Item> findAllItemOrderByQuantityDescAndPaging(Integer pageSize, Integer pageNum) {
        QueryWrapper<Item> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("quantity");
        Page<Item> page = new Page<>(pageNum,pageSize);
        return itemMapper.selectPage(page,queryWrapper).getRecords();
    }


    @Override
    public List<Item> findAllItemByPaging(Integer pageSize, Integer pageNum) {
        Page<Item> page = new Page<>(pageNum,pageSize);
        return itemMapper.selectPage(page,null).getRecords();
    }

    @Override
    public boolean cacheItemList(List<Item> itemList,long time) {
        for(Item item : itemList){
            redisUtil.set(redisUtil.createItemKey(item.getId()),item.getQuantity(),time);
        }
        return true;
    }


    /**
     *
     *
     *
     *
     * @param userId
     * @param itemId
     * @return
     * -1代表已经抢购过了，不能重复抢购
     * -2代表库存不足，无法购买
     * -3代表抢购失败
     * -4代表用户积分不足
     *  1代表购买成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int purchaseItem(Integer userId, Integer itemId,Integer price) {
        //优先判断积分是否满足
        User u = userService.findUserByUid(userId);
        if(u.getUserCredit() < price) return -4;
        //先去redis中查看有没有对应的商品缓存
        if (redisUtil.hasKey(redisUtil.createItemKey(itemId))) {
            //如果存在对应的缓存，那么查看有没有对应的购买记录
            if (redisUtil.hasKey(redisUtil.createItemPurchaseKey(userId, itemId))) {
                //如果存在了购买过此商品的key，那么不能再次购买
                return -1;
            }else{
                //执行对缓存的操作
                switch (redisUtil.purchaseItem(userId,itemId)){
                    case -1:
                        //代表库存为0，无法购买
                        return -2;

                    case 0:
                        //抢购失败
                        return -3;

                    case 1:
                        //抢购成功
                        //TODO:还要修改用户积分，修改Inventory表

                        break;
                }
            }
        }else{
            //对于不在缓存中的商品
            Item item = itemMapper.selectById(itemId);
            if(item.getQuantity() == 0){
                return -2;
            }
            item.setQuantity(item.getQuantity() - 1);
            while(itemMapper.updateById(item) == 0){
                //只要乐观锁发现数据有更改，那就再去查询
                item = itemMapper.selectById(itemId);
                if(item.getQuantity() == 0){
                    return -2;
                }
                item.setQuantity(item.getQuantity() - 1);
            }
        }

        //购买成功
        //TODO:还要修改用户积分，修改Inventory表
        u.setUserCredit(u.getUserCredit() - price);
        userService.updateUser(u);

        Item item = this.findOneById(itemId);
        Inventory inventory = new Inventory();
        ItemType itemType = item.getItemType();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, Math.toIntExact(itemType.getDurationTime()));
        inventory.setApproachOfAchieving("积分兑换")
                .setExpiryTime(calendar.getTime())
                .setItemId(itemId)
                .setUserId(userId)
                .setName(item.getName());
        inventoryService.addInventory(inventory);


        return 1;
    }


    /**
     *
     * @param itemId
     * @return
     */
    @Override
    public int findQuantityInCache(Integer itemId) {
        String key = redisUtil.createItemKey(itemId);
        if(!redisUtil.hasKey(key)){
            return -1;
        }
        return (int)redisUtil.get(key);

    }

    /**
     *
     * @param itemId
     * @return
     */
    @Override
    public int findQuantityInCacheAndPersist(Integer itemId) {
        String key = redisUtil.createItemKey(itemId);
        if(!redisUtil.hasKey(key)){
            return -1;
        }
        Item item = itemMapper.selectById(itemId);
        int cache = (int)redisUtil.get(key);
        int selt = item.getQuantity() - cache;
        item.setQuantity((int)redisUtil.get(key));

        return selt;
    }


}
