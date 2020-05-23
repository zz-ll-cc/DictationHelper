package com.dictation.mall.service;

import com.dictation.mall.entity.Inventory;
import org.apache.ibatis.annotations.ResultMap;

import java.util.List;

public interface InventoryService {


    boolean addInventory(Inventory inventory);

    boolean deleteInventory(Integer id);

    boolean updateInventory(Inventory inventory);

    Inventory findOneById(Integer id);

    //查询全部用户全部库存
    List<Inventory> findAll();

    List<Inventory> findAll(Integer pageSize,Integer pageNum);

    //查询某个user的库存
    List<Inventory> findAllByUserId(Integer userId,Integer pageSize,Integer pageNum);


    List<Inventory> findAllByUserId(Integer userId);

    //查询某个user的库存，按到期时间正序排序
    List<Inventory> findAllByUserIdOrderByExpiryTimeAsc(Integer userId,Integer pageSize,Integer pageNum);

    List<Inventory> findAllByUserIdOrderByExpiryTimeAsc(Integer userId);






}
