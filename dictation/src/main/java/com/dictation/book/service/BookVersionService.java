package com.dictation.book.service;

import com.dictation.book.entity.BookVersion;

import java.util.List;

public interface BookVersionService {



    public BookVersion findById(int bvId);

    public List<BookVersion> findAll();

    public List<BookVersion> findAllByPaging(int pageNum,int pageSize);
}
