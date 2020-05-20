package com.dictation;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.back.service.BackService;
import com.dictation.book.entity.Book;
import com.dictation.book.service.BookService;
import com.dictation.book.service.VersionService;
import com.dictation.book.service.WordService;
import com.dictation.record.service.DayRecordService;
import com.dictation.user.entity.User;
import com.dictation.user.entity.UserSignIn;
import com.dictation.user.service.UserService;
import com.dictation.util.FileUtil;
import com.dictation.util.RedisUtil;
import com.dictation.util.TimeUtil;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    @Autowired
    UserService userService;



    @Test
    void contextLoads() throws MessagingException, IOException, ParseException {

//        wordService.updatePic(10005,"sss");



//        JavaType javaType = new ObjectMapper().getTypeFactory().constructCollectionType(List.class,Book.class);
//        List<Book> bookList = new ObjectMapper().readValue((String) redisUtil.get("book:all"),javaType);
//        Page<Book> bookPage = new Page<>(1,2);
//        bookPage.setRecords(bookList);
//        System.out.println(bookPage.getRecords());


        //signin:{uid}:{week_of_year}
        //activeuser:simpleDateFormat.format(date);
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_YEAR,1);
//        Date date = calendar.getTime();
//
//        System.out.println(simpleDateFormat.format(date));

//        calendar.setTime(new Date());
//        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        if(dayOfWeek != 1){
//            dayOfWeek -= 1;
//        }else{
//            dayOfWeek = 7;
//        }
//        for(Boolean b : redisUtil.getBitList("signin:" + "123" + ":" + simpleDateFormat.format(date))){
//            System.out.println(b);
//        }

//        System.out.println(TimeUtil.getSecondsToNextMonday4pm()/1000);





    }


    @Test
    void name() {
        System.out.println(userService.findUserCreditRecordByPaging(10124, 2, 1));
    }
}
