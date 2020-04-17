package com.dictation.book.service;

import com.dictation.book.entity.Word;

import java.util.List;

public interface WordService {

    boolean save(Word word);
    boolean delete(Word word);
    boolean delete(Integer id);
    boolean update(Word word);
    boolean updatePic(int wid , String url);
    Word findById(int id);
    List<Word> findAllByPaging(int pageNum,int pageSize);
    List<Word> findAll();
    List<Word> findAllByBid(int bid);
    List<Word> findAllByBidAndUnid(int bid,int unid);
    int findBookWordsTotal(int bid);


}
