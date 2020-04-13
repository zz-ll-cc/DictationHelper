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
        return gradeMapper.findAll();
    }

}
