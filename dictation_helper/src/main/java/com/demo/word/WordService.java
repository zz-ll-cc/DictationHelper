package com.demo.word;

import com.demo.common.model.TblWord;

import java.util.List;

public class WordService {
    private TblWord dao=new TblWord().dao();


    public List<TblWord> findAllWords(){
        return dao.findAll();
    }
    public List<TblWord> findAllWordsByBook(int bid){
        return dao.find("select * from tbl_word where bid = ?",bid);
    }
    public List<TblWord> findAllWordsByBookAndUnit(int bid,int unid){
        return dao.find("select * from tbl_word where bid = ? and unid = ?",bid,unid);
    }
}
