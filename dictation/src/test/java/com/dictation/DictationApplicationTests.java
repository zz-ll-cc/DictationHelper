package com.dictation;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.back.service.BackService;
import com.dictation.book.entity.Book;
import com.dictation.book.service.BookService;
import com.dictation.book.service.VersionService;
import com.dictation.book.service.WordService;
import com.dictation.record.service.DayRecordService;
import com.dictation.user.entity.User;
import com.dictation.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@SpringBootTest
class DictationApplicationTests {

    @Autowired
    BackService backService;

    @Autowired
    BookService bookService;

    @Autowired
    VersionService versionService;

    @Autowired
    WordService wordService;

    @Autowired
    DayRecordService dayRecordService;

    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Autowired
    RedisUtil redisUtil;



    @Test
    void contextLoads() throws MessagingException, IOException {

//        wordService.updatePic(10005,"sss");



        JavaType javaType = new ObjectMapper().getTypeFactory().constructCollectionType(List.class,Book.class);
        List<Book> bookList = new ObjectMapper().readValue((String) redisUtil.get("book:all"),javaType);
        Page<Book> bookPage = new Page<>(1,2);
        bookPage.setRecords(bookList);
        System.out.println(bookPage.getRecords());


    }

}
