package com.dictation.mapper;

import com.dictation.book.entity.Word;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface WordMapper {

    public int insert(Word word);
    public int delete(Word word);
    public int update(Word word);
    public int updatePic(@Param("wid") int wid ,@Param("wimgPath") String url);
    public Word findById(@Param("wid") int wid);
    public List<Word> findAll();
    public List<Word> findByBid(@Param("bid") int bid);
    public List<Word> findByBidAndUnid(@Param("bid") int bid , @Param("unId") int unId);








}
