package com.dictation.mall.controller;

import com.dictation.mall.entity.Item;
import com.dictation.mall.service.ItemService;
import com.dictation.util.FileUtil;
import com.dictation.util.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ItemController
 * @Description
 * @Author zlc
 * @Date 2020-05-23 00:46
 */
@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    QiniuUtil qiniuUtil;

    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("items",itemService.findAllItem());
        return "item";
    }

    @RequestMapping("/add")
    public String add(@RequestParam("name") String name,
                      @RequestParam("cover")MultipartFile cover,
                      @RequestParam("description") String description,
                      @RequestParam("quantity") Integer quantity,
                      @RequestParam("itemType") Integer itemType,
                      @RequestParam("price") Integer price){

        File f;
        InputStream ins = null;
        try {
            ins = cover.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        f = new File(cover.getOriginalFilename());
        FileUtil.inputStreamToFile(ins,f);
        String url = null;
        try {
            url = qiniuUtil.saveImage(f,cover.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("success: imageUrl = "+url);

        Item item = new Item();
        item.setQuantity(quantity).setCover(url).setDescription(description).setItemTypeId(itemType).setName(name).setPrice(price);
        itemService.addItem(item);
        f.delete();

        return "item";
    }



    @RequestMapping("/cacheItem")
    @ResponseBody
    public String cacheItem(@RequestParam("itemId") Integer itemId,@RequestParam(value = "time",required = false) Long time){
        long expire_time = 60*60*1;//默认一小时
        if(time != null){
            expire_time = time;
        }
        Item item = itemService.findOneById(itemId);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        itemService.cacheItemList(itemList,expire_time);

        return "商品缓存成功，id为：" + itemId + ",失效时间为：" + expire_time + "秒";
    }


    //购买商品
    @RequestMapping("/purchase")
    @ResponseBody
    public String purchase(@RequestParam("userId") Integer userId,@RequestParam("itemId") Integer itemId,@RequestParam("price") Integer price){
        switch (itemService.purchaseItem(userId,itemId,price)){

            case 1:
                return "购买成功";

            case -1:
                return "已经抢购过了，不能重复抢购";

            case -2:
                return "库存不足，无法购买";

            case -3:
                return "抢购失败";

            case -4:
                return "积分不足";


        }
        return null;
    }



}
