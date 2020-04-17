package com.dictation.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.book.entity.Book;
import com.dictation.book.service.BookService;
import com.dictation.mapper.BookMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName BookServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-04-13 11:31
 */
@Service("bookService")
public class BookServiceImpl implements BookService {

    @Autowired
    BookMapper bookMapper;

    @Override
    public List<Book> findAll() {
        return bookMapper.selectList(null);
    }


    @Override
    public List<Book> findAllByPaging(int pageNum, int pageSize) {
        Page<Book> page = new Page<>(pageNum,pageSize);
        return bookMapper.selectPage(page,null).getRecords();
    }

    @Override
    public List<Book> findAllByVesion(int bvid) {
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.eq("version_id",bvid);
        return bookMapper.selectList(bookQueryWrapper);
    }

    @Override
    public List<Book> findAllByGrade(int gid) {
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.eq("grade_id",gid);
        return bookMapper.selectList(bookQueryWrapper);
    }

    @Override
    public List<Book> findAllByVesionAndGrade(int bvid, int gid) {
        QueryWrapper<Book> bookQueryWrapper = new QueryWrapper<>();
        bookQueryWrapper.eq("grade_id",gid).eq("version_id",bvid);
        return bookMapper.selectList(bookQueryWrapper);
    }

    @Override
    public boolean updateUrl(int bid, String url) {
        Book book = bookMapper.selectById(bid);
        book.setBookCover(url);
        return bookMapper.updateById(book) == 1 ;
    }

    @Override
    public boolean saveOne(Book book) {
        return bookMapper.insert(book) == 1 ;
    }

    @Override
    public boolean delete(Book book) {
        return bookMapper.deleteById(book.getId()) == 1;
    }

    @Override
    public boolean delete(Integer id) {
        return bookMapper.deleteById(id) == 1;
    }
}
