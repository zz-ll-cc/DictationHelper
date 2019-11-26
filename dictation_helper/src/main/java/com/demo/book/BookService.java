package com.demo.book;

import com.demo.common.model.Book;
import com.demo.common.model.Version;

import java.util.List;

public class BookService {

    private Book dao = new Book().dao();


    public List<Book> findAllBook(){
        return dao.findAll();
    }


}
