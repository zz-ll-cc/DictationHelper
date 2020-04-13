package com.dictation.mapper;


import com.dictation.book.entity.Book;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface BookMapper{

    public Book findById(@Param("bid") int bid);
    public List<Book> findAll();
    public List<Book> findAllByVesion(@Param("bvid") int bvid);
    public List<Book> findAllByGrade(@Param("gid") int gid);
    public List<Book> findAllByVesionAndGrade(@Param("bvid") int bvid,@Param("gid") int gid);
    public int deleteById(@Param("bid") int bid);
    public int delete(Book book);
    public int update(Book book);
    public int insert(Book book);

}
