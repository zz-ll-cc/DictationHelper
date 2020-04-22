package com.dictation.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.book.entity.Word;
import com.dictation.book.service.WordService;
import com.dictation.mapper.WordMapper;
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
    public boolean save(Word word) {
        return wordMapper.insert(word) == 1;
    }

    @Override
    public boolean delete(Word word) {
        return wordMapper.deleteById(word.getWid()) == 1;
    }

    @Override
    public boolean delete(Integer id) {
        return wordMapper.deleteById(id) == 1;
    }

    @Override
    public boolean update(Word word) {
        return wordMapper.updateById(word) == 1;
    }

    @Override
    public boolean updatePic(int wid, String url) {
        Word word = wordMapper.selectById(wid);
        word.setWimgPath(url);
        return wordMapper.updateById(word) == 1;
    }

    @Override
    public Word findById(int id) {
        return wordMapper.selectById(id);
    }

    @Override
    public List<Word> findAllByPaging(int pageNum, int pageSize) {
        Page<Word> wordPage = new Page<>(pageNum,pageSize);
        return wordMapper.selectPage(wordPage,null).getRecords();
    }

    @Override
    public List<Word> findAll() {
        return wordMapper.selectList(null);
    }

    @Override
    public List<Word> findAllByBid(int bid) {
        QueryWrapper<Word> wordQueryWrapper = new QueryWrapper<>();
        wordQueryWrapper.eq("book_id",bid);
        return wordMapper.selectList(wordQueryWrapper);
    }

    @Override
    public List<Word> findAllByBidAndUnid(int bid, int unid) {
        QueryWrapper<Word> wordQueryWrapper = new QueryWrapper<>();
        wordQueryWrapper.eq("book_id",bid).eq("unit_id",unid);
        return wordMapper.selectList(wordQueryWrapper);
    }

    @Override
    public int findBookWordsTotal(int bid) {
        QueryWrapper<Word> wordQueryWrapper = new QueryWrapper<>();
        wordQueryWrapper.eq("book_id",bid);
        return wordMapper.selectList(wordQueryWrapper).size();
    }
}
