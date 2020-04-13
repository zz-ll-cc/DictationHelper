package com.dictation.book.service;

import com.dictation.book.entity.Book;

import java.util.List;

public interface BookService {

    public List<Book> findAll();
    public List<Book> findAllByPaging(int pageNum,int pageSize);
    public List<Book> findAllByVesion(int bvid);
    public List<Book> findAllByGrade(int gid);
    public List<Book> findAllByVesionAndGrade(int bvid,int gid);
    public Book updateUrl(int bid,String url);
    public Book saveOne(Book book);
    public boolean delete(Book book);


}
