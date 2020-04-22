package com.dictation;

import com.dictation.back.service.BackService;
import com.dictation.book.service.BookService;
import com.dictation.book.service.VersionService;
import com.dictation.book.service.WordService;
import com.dictation.record.service.DayRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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



    @Test
    void contextLoads() throws MessagingException {

//        wordService.updatePic(10005,"sss");


    }

}
