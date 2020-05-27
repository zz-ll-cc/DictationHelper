package com.dictation.redeem.service;

import com.dictation.redeem.entity.Redeem;

import java.util.Date;
import java.util.List;

public interface RedeemService {


    //批量插入兑换码
    int insertBatch(List<Redeem> redeems);

    //查询当前类型的兑换码还剩余多少未使用
    int findUnusedByTypeId(Integer typeId);

    //更新某个兑换码
    int updateOne(Redeem redeem);

    //使某一个类型的兑换码全部失效
    int updateBatch(Integer typeId);

    //使某一个日期之前的兑换码全部失效
    int updateBatch(Date date);

    //查询某个验证码是否使用过，如果没使用过，修改为使用过
    boolean useOne(String value,Integer userId);

    //获取一个没有使用过的兑换码
    String getOneByTypeId(Integer typeId);

}
