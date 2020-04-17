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
        return unitMapper.selectList(null);
    }

    @Override
    public boolean save(Unit unit) {
        return unitMapper.insert(unit) == 1;
    }

    @Override
    public boolean delete(Unit unit) {
        return unitMapper.deleteById(unit.getId()) == 1;
    }

    @Override
    public boolean delete(Integer id) {
        return unitMapper.deleteById(id) == 1;
    }

    @Override
    public boolean update(Unit unit) {
        return unitMapper.updateById(unit) ==1;
    }
}
