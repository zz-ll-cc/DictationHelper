package com.dictation.book.service;

import com.dictation.book.entity.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();
    List<Book> findAllByPaging(int pageNum,int pageSize);
    List<Book> findAllByVesion(int bvid);
    List<Book> findAllByGrade(int gid);
    List<Book> findAllByVesionAndGrade(int bvid,int gid);
    boolean updateUrl(int bid,String url);
    boolean saveOne(Book book);
    boolean delete(Book book);
    boolean delete(Integer id);
    boolean checkBookVersion(int bid,int version);
    Book findOneById(Integer id);





}
