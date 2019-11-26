package com.demo.book;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

public class BookController extends Controller {

    @Inject
    private BookService bookService;

    public void index(){
        renderJson(bookService.findAllBook().toString());
    }


}
