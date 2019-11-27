package com.demo.word;

import com.demo.common.model.TblWord;

import java.util.List;

public class WordService {
    private TblWord dao=new TblWord().dao();
    private TblWord word=new TblWord();

    public List<TblWord> findAllWords(){
        return dao.findAll();
    }
    public List<TblWord> findAllWordsByBook(int bid){
        return dao.find("select * from tbl_word where bid = ?",bid);
    }
    public List<TblWord> findAllWordsByBookAndUnit(int bid,int unid){
        return dao.find("select * from tbl_word where bid = ? and unid = ?",bid,unid);
    }
    public void update(int wid,String url){
        word.findById(wid).set("wimgPath",url).update();
    }
}
