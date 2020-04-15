package com.dictation.book.service;

import com.dictation.book.entity.Word;

import java.util.List;

public interface WordService {

    public Word save(Word word);
    public boolean delete(Word word);
    public boolean update(Word word);
    public boolean updatePic(int wid , String url);
    public Word findById(int id);
    public List<Word> findAllByPaging(int pageNum,int pageSize);
    public List<Word> findAll();
    public List<Word> findAllByBid(int bid);
    public List<Word> findAllByBidAndUnid(int bid,int unid);
    public int findBookWordsTotal(int bid);


}
