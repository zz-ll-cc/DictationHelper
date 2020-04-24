package com.dictation.book.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.book.entity.Book;
import com.dictation.book.service.BookService;
import com.dictation.mapper.BookMapper;
import com.dictation.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<Book> findAll() {
        String result;
        List<Book> bookList = null;
        try {
            if((result = (String) redisUtil.get("book:all")) == null){
                bookList = bookMapper.selectList(null);
                redisUtil.set("book:all",new ObjectMapper().writeValueAsString(bookList),60*60*12);
                return bookList;
            } else {
                JavaType javaType = new ObjectMapper().getTypeFactory().constructCollectionType(List.class,Book.class);
                return new ObjectMapper().readValue(result,javaType);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public List<Book> findAllByPaging(int pageNum, int pageSize) {
        Page<Book> page = new Page<>(pageNum,pageSize);
        return bookMapper.selectPage(page,null).getRecords();
    }


    /**
     * 先去缓存找
     * @param bvid
     * @return
     */
    @Override
    public List<Book> findAllByVesion(int bvid) {
        List<Book> bookList = findAll();
        List<Book> bookResult = new ArrayList<>();

        bookList.stream()
                .filter(book -> book.getBvid()==bvid)
                .forEach(book ->bookResult.add(book));
        return bookResult;
    }

    @Override
    public List<Book> findAllByGrade(int gid) {
        List<Book> bookList = findAll();
        List<Book> bookResult = new ArrayList<>();

        bookList.stream()
                .filter(book -> book.getGid() == gid)
                .forEach(book -> bookResult.add(book));

        return bookResult;
    }

    @Override
    public List<Book> findAllByVesionAndGrade(int bvid, int gid) {
        List<Book> bookList = findAll();
        List<Book> bookResult = new ArrayList<>();

        bookList.stream()
                .filter(book -> book.getGid() == gid && book.getBvid()==bvid)
                .forEach(book -> bookResult.add(book));

        return bookResult;
    }

    @Override
    public boolean updateUrl(int bid, String url) {
        Book book = bookMapper.selectById(bid);
        book.setBimgPath(url);
        return bookMapper.updateById(book) == 1 ;
    }

    @Override
    public boolean saveOne(Book book) {
        return bookMapper.insert(book) == 1 ;
    }

    @Override
    public boolean delete(Book book) {
        return bookMapper.deleteById(book.getBid()) == 1;
    }

    @Override
    public boolean delete(Integer id) {
        return bookMapper.deleteById(id) == 1;
    }
}
