package com.demo.word;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

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
}
