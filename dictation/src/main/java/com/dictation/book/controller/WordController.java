package com.dictation.book.controller;

import com.dictation.book.entity.Word;
import com.dictation.book.service.WordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @ClassName WordController
 * @Description
 * @Author zlc
 * @Date 2020-04-14 13:49
 */
@RestController
@RequestMapping("/word")
public class WordController {

    @Autowired
    WordService wordService;

    @RequestMapping("/findwordsbybook")
    public List<Word> findwordsbybook(@RequestParam("bid") int bid){
        return wordService.findAllByBid(bid);
    }

    @RequestMapping("/findwordsbybookandunit")
    public List<Word> findwordsbybookandunit(@RequestParam("bid") int bid , @RequestParam("unid") int unid){
        return wordService.findAllByBidAndUnid(bid , unid);
    }

    @RequestMapping("/findallwords")
    public List<Word> findallwords(){
        return wordService.findAll();
    }

    @RequestMapping("/findallwordsrandom")
    public List<Word> findallwordsrandom(){
        List<Word> words = wordService.findAll();
        Collections.shuffle(words);
        return words;
    }

    @RequestMapping("/findwordsbybookrandom")
    public List<Word> findwordsbybookrandom(@RequestParam("bid") int bid){
        List<Word> words=wordService.findAllByBid(bid);
        Collections.shuffle(words);
        return words;
    }

    @RequestMapping("/findwordsbybookandunitrandom")
    public List<Word> findwordsbybookandunitrandom(@RequestParam("bid") int bid , @RequestParam("unid") int unid){
        List<Word> words=wordService.findAllByBidAndUnid(bid , unid);
        Collections.shuffle(words);
        return words;
    }

    @RequestMapping("/getsumbybid")
    public int getsumbybid(@RequestParam("bid") int bid){
        return wordService.findBookWordsTotal(bid);
    }

}
