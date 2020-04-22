package com.dictation.book.controller;

import com.dictation.book.entity.Book;
import com.dictation.book.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName BookController
 * @Description
 * @Author zlc
 * @Date 2020-04-14 12:20
 */
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    @RequestMapping("/all")
    public List<Book> all(){
    	return bookService.findAll();
    }

    @RequestMapping("/findbyversion")
    public List<Book> findbyversion(HttpServletRequest request){
        int bvid = 0;
        List<Book> bookList = null;
        try{
            bvid = Integer.parseInt(request.getParameter("bvid"));
            bookList = bookService.findAllByVesion(bvid);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return bookList;
        }
    }

    @RequestMapping("/findbygrade")
    public List<Book> findbygrade(HttpServletRequest request){
        int gid = 0;
        List<Book> bookList = null;
        try{
            gid = Integer.parseInt(request.getParameter("gid"));
            bookList = bookService.findAllByGrade(gid);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return bookList;
        }
    }

    @RequestMapping("/findbyversionandgrade")
    public List<Book> findbyversionandgrade(@RequestParam("bvid") int bvid , @RequestParam("gid") int gid){
        if (bvid == 1 && gid == 1){
            return bookService.findAll();
        }
        else if(bvid == 1){
            return bookService.findAllByGrade(gid);
        }
        else if(gid == 1){
            return bookService.findAllByVesion(bvid);
        }
        else {
            return bookService.findAllByVesionAndGrade(bvid,gid);
        }
    }



}
