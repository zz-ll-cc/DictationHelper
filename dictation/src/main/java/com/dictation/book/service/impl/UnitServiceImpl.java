package com.dictation.book.service.impl;

import com.dictation.book.entity.Unit;
import com.dictation.book.service.UnitService;
import com.dictation.mapper.UnitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName UnitServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:19
 */
@Service("unitService")
public class UnitServiceImpl implements UnitService {

    @Autowired
    UnitMapper unitMapper;


    @Override
    public List<Unit> findAll() {
        return unitMapper.findAll();
    }

}
