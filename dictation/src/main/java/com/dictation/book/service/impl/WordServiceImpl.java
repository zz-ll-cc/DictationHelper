package com.dictation.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.book.entity.Book;
import com.dictation.book.entity.Word;
import com.dictation.book.service.WordService;
import com.dictation.mapper.BookMapper;
import com.dictation.mapper.WordMapper;
import com.dictation.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.Executors;

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

    @Autowired
    BookMapper bookMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean save(Word word) {
        try {
            wordMapper.insert(word);
            Book book = bookMapper.selectById(word.getBid());
            book.setBookWordVersion(book.getBookWordVersion()+1);
            bookMapper.updateById(book);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Word word) {
        Book book = bookMapper.selectById(word.getBid());
        book.setBookWordVersion(book.getBookWordVersion()+1);
        bookMapper.updateById(book);
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


    /**
     * 把单词存入缓存
     * @return
     */
    @Override
    public List<Word> findAll() {
        String wStr;
        List<Word> result = null;
        try {
            if((wStr = (String) redisUtil.get("word:all")) != null){
                JavaType javaType = new ObjectMapper().getTypeFactory().constructCollectionType(List.class,Word.class);
                result = new ObjectMapper().readValue(wStr,javaType);
                return result;
            }else{
                if((result = wordMapper.selectList(null)) != null){
                    redisUtil.set("word:all",new ObjectMapper().writeValueAsString(result),60*60*12);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }finally {
            return result;
        }
    }

    @Override
    public List<Word> findAllByBid(int bid) {
        List<Word> result = null;
        String wStr;

        try {
            if((wStr = (String) redisUtil.get("word:" + bid)) != null){
                JavaType javaType = new ObjectMapper().getTypeFactory().constructCollectionType(List.class,Word.class);
                result = new ObjectMapper().readValue(wStr,javaType);
            }else{
                QueryWrapper<Word> wordQueryWrapper = new QueryWrapper<>();
                wordQueryWrapper.eq("book_id",bid);
                if((result = wordMapper.selectList(wordQueryWrapper)) != null){
                    redisUtil.set("word:" + bid,new ObjectMapper().writeValueAsString(result),60*60*12);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }finally {
            return result;
        }


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
