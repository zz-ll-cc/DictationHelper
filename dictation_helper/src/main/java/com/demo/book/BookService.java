package com.demo.book;

import com.demo.common.model.TblBook;

import java.util.List;

public class BookService {

    private TblBook dao = new TblBook().dao();


    public List<TblBook> findAllBook(){
        return dao.findAll();
    }


}
