package com.dictation.book.controller;

import com.dictation.book.entity.Book;
import com.dictation.book.entity.Word;
import com.dictation.book.service.BookService;
import com.dictation.book.service.WordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    WordService wordService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
            return bookService.findAllByVesionAndGrade(bvid, gid);
        }

    }


    @RequestMapping("/checkbookversion")
    public String checkBookVersion(@RequestParam("bid") int bid, @RequestParam(value = "version",required = false) Integer bookWordVersion){
        if(bookWordVersion == null){
            //代表查询单本书
            try {
                return new ObjectMapper().writeValueAsString(bookService.findOneById(bid));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                logger.warn("bookcontroller中的checkbookversion方法当没有穿version字段时json转换出错");
                return null;
            }

        }
        Map<String,Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if(!bookService.checkBookVersion(bid,bookWordVersion)){
            map.put("type",1);
            map.put("word",wordService.findAllByBid(bid));
            map.put("version",bookService.findOneById(bid).getBookWordVersion());
        }else{
            map.put("type",0);
            map.put("word",null);
            map.put("version",null);
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }








}
