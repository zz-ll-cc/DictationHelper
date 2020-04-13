package com.dictation.mapper;

import com.dictation.book.entity.BookVersion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BookVersionMapper {

    public BookVersion findbyId(@Param("bvId") int bvId);

    public List<BookVersion> findAll();

    public int insert(BookVersion bookVersion);
    public int delete(BookVersion bookVersion);
    public int update(BookVersion bookVersion);


}
