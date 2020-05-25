package com.dictation.mall.service;

import com.dictation.mall.entity.Item;

import java.util.List;

public interface ItemService {

    boolean addItem(Item item);

    boolean deleteItem(Integer id);

    boolean deleteItem(Item item);

    boolean updateItem(Item item);

    Item findOneById(Integer id);

    List<Item> findAllItem();

    //根据创建时间倒序分页查询商品
    List<Item> findAllItemOrderByCreateTimeDescAndPaging(Integer pageSize,Integer pageNum);

    //根据剩余数量倒序分页查询商品
    List<Item> findAllItemOrderByQuantityDescAndPaging(Integer pageSize,Integer pageNum);

    //分页查询商品
    List<Item> findAllItemByPaging(Integer pageSize,Integer pageNum);

    //缓存一些商品到redis中
    boolean cacheItemList(List<Item> itemList,long time);

    //用户购买商品
    int purchaseItem(Integer userId,Integer itemId,Integer price);

    //获取当前商品在缓存中的库存并返回
    int findQuantityInCache(Integer itemId);

    //获取当前商品在缓存中的库存并持久化到数据库,返回差量
    int findQuantityInCacheAndPersist(Integer itemId);



}
