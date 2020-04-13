package com.dictation.book.service.impl;

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
        return bookMapper.findAll();
    }


    @Override
    public List<Book> findAllByPaging(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return bookMapper.findAll();
    }

    @Override
    public List<Book> findAllByVesion(int bvid) {
        return bookMapper.findAllByVesion(bvid);
    }

    @Override
    public List<Book> findAllByGrade(int gid) {
        return bookMapper.findAllByGrade(gid);
    }

    @Override
    public List<Book> findAllByVesionAndGrade(int bvid, int gid) {
        return bookMapper.findAllByVesionAndGrade(bvid,gid);
    }

    @Override
    public Book updateUrl(int bid, String url) {
        Book book = bookMapper.findById(bid);
        if(book != null) {
            book.setBimgPath(url);
            bookMapper.update(book);
            return book;
        }
        return null;
    }

    @Override
    public Book saveOne(Book book) {
        bookMapper.insert(book);
        return book;
    }

    @Override
    public boolean delete(Book book) {
        return bookMapper.delete(book) == 1;
    }

}
