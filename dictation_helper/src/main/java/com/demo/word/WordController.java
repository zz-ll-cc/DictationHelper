package com.demo.word;

import com.demo.common.model.TblWord;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

import java.util.Collections;
import java.util.List;

public class WordController extends Controller {
    @Inject
    private WordService wordService;
    public void findwordsbybook(){
        int bid=getParaToInt();
        System.out.println(bid);
        renderJson(wordService.findAllWordsByBook(bid));
    }

    public void findwordsbybookandunit(){
        String unid=getPara("unid");
        String bid=getPara("bid");
        renderJson(wordService.findAllWordsByBookAndUnit(Integer.parseInt(bid),Integer.parseInt(unid)));
    }
    public void findallwords(){
        renderJson(wordService.findAllWords());
    }
    public void findallwordsrandom(){
        List<TblWord> words=wordService.findAllWords();
        Collections.shuffle(words);
        renderJson(words);
    }
    public void findwordsbybookrandom(){
        int bid=getParaToInt();
        System.out.println(bid);
        List<TblWord> words=wordService.findAllWordsByBook(bid);
        Collections.shuffle(words);
        renderJson(words);
    }
    public void findwordsbybookandunitrandom(){
        String unid=getPara("unid");
        String bid=getPara("bid");
        List<TblWord> words=wordService.findAllWordsByBookAndUnit(Integer.parseInt(bid),Integer.parseInt(unid));
        Collections.shuffle(words);
        renderJson(words);
    }
    public void getsumbybid(){
        int bid=getInt("bid");
        renderJson(wordService.getSum(bid));
    }
}
