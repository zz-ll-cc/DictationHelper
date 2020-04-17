package com.dictation.book.service.impl;

import com.dictation.book.entity.Grade;
import com.dictation.book.service.GradeService;
import com.dictation.mapper.GradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName GradeServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:14
 */
@Service("gradeService")
public class GradeServiceImpl implements GradeService {

    @Autowired
    GradeMapper gradeMapper;


    @Override
    public List<Grade> findAll() {
        return gradeMapper.selectList(null);
    }

    @Override
    public boolean save(Grade grade) {
        return gradeMapper.insert(grade) == 1;
    }

    @Override
    public boolean delete(Grade grade) {
        return gradeMapper.deleteById(grade.getId()) == 1;
    }



    @Override
    public boolean delete(Integer id) {
        return gradeMapper.deleteById(id) == 1;
    }

    @Override
    public boolean update(Grade grade) {
        return gradeMapper.updateById(grade) == 1;
    }
}
