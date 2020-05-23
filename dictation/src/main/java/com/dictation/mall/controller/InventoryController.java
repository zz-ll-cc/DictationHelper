package com.dictation.mall.controller;

import com.dictation.mall.entity.Inventory;
import com.dictation.mall.service.InventoryService;
import com.dictation.mall.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName InventoryController
 * @Description
 * @Author zlc
 * @Date 2020-05-23 19:19
 */

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    ItemService itemService;

    @Autowired
    InventoryService inventoryService;



    @RequestMapping("/getMyInventory")
    public List<Inventory> getMyInventory(@RequestParam("userId") Integer userId, @RequestParam(value = "pageSize",required = false) Integer pageSize, @RequestParam(value = "pageNum",required = false) Integer pageNum){
        if(pageNum == null && pageNum < 1){
            pageNum = 1;
        }
        if(pageSize == null && pageSize < 1){
            pageSize = 10;
        }
        return inventoryService.findAllByUserId(userId, pageSize, pageNum);
    }


}
