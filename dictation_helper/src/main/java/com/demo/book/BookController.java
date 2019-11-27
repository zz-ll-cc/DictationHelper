package com.demo.book;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

public class BookController extends Controller {

    @Inject
    private BookService bookService;

    public void findallbooks(){
        renderJson(bookService.findAllBooks());
    }

    public void findbooksbyversion(){
        int bvid=getParaToInt();
        renderJson(bookService.findAllBooksByVesion(bvid));
    }
    public void findbooksbyversionandgrade(){
        String bvid=getPara("bvid");
        String gid=getPara("gid");
        renderJson(bookService.findAllBooksByVesionAndGrade(Integer.parseInt(bvid),Integer.parseInt(gid)));
    }
}
