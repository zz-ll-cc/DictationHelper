package com.dictation.redeem.controller;

import com.dictation.mall.service.InventoryService;
import com.dictation.redeem.service.RedeemService;
import com.dictation.util.RedeemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName RedeemController
 * @Description
 * @Author zlc
 * @Date 2020-05-27 16:19
 */
@Controller
@RequestMapping("/redeem")
public class RedeemController {



    @Autowired
    RedeemService redeemService;

    @Autowired
    RedeemUtil redeemUtil;

    @Autowired
    InventoryService inventoryService;


    @RequestMapping("/create")
    @ResponseBody
    public String create(@RequestParam("typeId") Integer typeId,@RequestParam("amount")Integer amount,@RequestParam(value = "length",required = false)Integer length){
        System.out.println(redeemService.insertBatch(redeemUtil.create(typeId.byteValue(), amount, 12, "haohaoxuexi")));

        return "ok";

    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verify(@RequestParam("typeId") Integer typeId,@RequestParam("redeemString")String redeemString,@RequestParam("userId") Integer userId){
        switch(redeemUtil.VerifyCode(typeId.byteValue(),redeemString)){
            case -1:
                //兑换码类型错误
                return"兑换码类型错误";
            case 1:
                //兑换成功
                if(!redeemService.useOne(redeemString,userId))return "兑换码已经被使用过了";
                return "兑换码使用成功，获取畅读券一张";

            default:
                return "兑换码错误";
        }
    }

    @RequestMapping("/getOne")
    @ResponseBody
    public String getOne(){
        return  redeemService.getOneByTypeId(1);
    }




}
