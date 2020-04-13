package com.dictation;

import com.dictation.back.entity.Back;
import com.dictation.back.service.BackService;
import com.dictation.book.entity.Book;
import com.dictation.book.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class DictationApplicationTests {

    @Autowired
    BackService backService;

    @Autowired
    BookService bookService;

    @Test
    void contextLoads() {
//        List<Book> books = bookService.findAllByVesionAndGrade(10000,10000);
//        for(Book b : books){
//            System.out.println(b);
//        }


    }

}
