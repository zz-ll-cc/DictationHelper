package com.demo.book;

import com.demo.common.model.TblBook;

import java.util.List;

public class BookService {

    private TblBook dao = new TblBook().dao();


    public List<TblBook> findAllBooks(){
        return dao.findAll();
    }
    public List<TblBook> findAllBooksByVesion(int bvid){
        return dao.find("select * from tbl_book where bvid = ?",bvid);
    }
    public List<TblBook> findAllBooksByVesionAndGrade(int bvid,int gid){
        return dao.find("select * from tbl_book where bvid = ? and gid =?",bvid,gid);
    }

}
