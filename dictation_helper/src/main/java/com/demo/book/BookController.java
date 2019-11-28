package com.demo.book;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

public class BookController extends Controller {

    @Inject
    private BookService bookService;

    public void all(){
        renderJson(bookService.findAllBooks());
    }

    public void findbyversion(){
        int bvid=getParaToInt();
        renderJson(bookService.findAllBooksByVesion(bvid));
    }
    public void findbygrade(){
        int gid=getParaToInt();
        renderJson(bookService.findAllBooksByVesion(gid));
    }
    public void findbyversionandgrade(){
        String bvid=getPara("bvid");
        String gid=getPara("gid");
        int bv=Integer.parseInt(bvid);
        int g=Integer.parseInt(gid);
        System.out.println(bv);
        System.out.println(g);
        if (bv==1&&g==1){
            renderJson(bookService.findAllBooks());
        }
        else if(bv==1){
            System.out.println(bv+"   "+g);
            renderJson(bookService.findAllBooksByGrade(g));
        }
        else if(g==1){
            renderJson(bookService.findAllBooksByVesion(bv));
        }
        else {
            renderJson(bookService.findAllBooksByVesionAndGrade(bv, g));
        }
    }
}
