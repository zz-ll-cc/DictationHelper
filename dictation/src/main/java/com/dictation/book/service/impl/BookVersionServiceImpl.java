package com.dictation.book.service.impl;

import com.dictation.book.entity.BookVersion;
import com.dictation.book.service.BookVersionService;
import com.dictation.mapper.BookVersionMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName BookVersionServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-04-13 14:49
 */
@Service("bookVersionService")
public class BookVersionServiceImpl implements BookVersionService {

    @Autowired
    BookVersionMapper bookVersionMapper;

    @Override
    public BookVersion findById(int bvId) {
        return bookVersionMapper.findbyId(bvId);
    }

    @Override
    public List<BookVersion> findAll() {
        return bookVersionMapper.findAll();
    }

    @Override
    public List<BookVersion> findAllByPaging(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return bookVersionMapper.findAll();
    }
}
