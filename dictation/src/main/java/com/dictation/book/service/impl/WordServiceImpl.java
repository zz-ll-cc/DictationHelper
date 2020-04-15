package com.dictation.book.service.impl;

import com.dictation.book.entity.Word;
import com.dictation.book.service.WordService;
import com.dictation.mapper.WordMapper;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName WordServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-04-13 17:45
 */
@Service("wordService")
public class WordServiceImpl implements WordService {

    @Autowired
    WordMapper wordMapper;

    @Override
    public Word save(Word word) {
        wordMapper.insert(word);
        return word;
    }

    @Override
    public boolean delete(Word word) {
        return wordMapper.delete(word) == 1;
    }

    @Override
    public boolean update(Word word) {
        return wordMapper.update(word) == 1;
    }

    @Override
    public boolean updatePic(int wid, String url) {
        return wordMapper.updatePic(wid,url) == 1;
    }

    @Override
    public Word findById(int id) {
        return wordMapper.findById(id);
    }

    @Override
    public List<Word> findAllByPaging(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return wordMapper.findAll();
    }

    @Override
    public List<Word> findAll() {
        return wordMapper.findAll();
    }

    @Override
    public List<Word> findAllByBid(int bid) {
        return wordMapper.findByBid(bid);
    }

    @Override
    public List<Word> findAllByBidAndUnid(int bid, int unid) {
        return wordMapper.findByBidAndUnid(bid,unid);
    }

    @Override
    public int findBookWordsTotal(int bid) {
        return wordMapper.findByBid(bid).size();
    }
}
