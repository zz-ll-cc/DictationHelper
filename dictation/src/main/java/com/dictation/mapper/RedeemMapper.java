package com.dictation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dictation.redeem.entity.Redeem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RedeemMapper extends BaseMapper<Redeem> {

    Integer insertBatch(List<Redeem> redeems);

}
