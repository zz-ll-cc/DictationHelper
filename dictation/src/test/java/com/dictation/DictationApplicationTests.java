package com.dictation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dictation.back.service.BackService;
import com.dictation.book.service.BookService;
import com.dictation.book.service.VersionService;
import com.dictation.book.service.WordService;
import com.dictation.mall.entity.Item;
import com.dictation.mapper.CreditRecordMapper;
import com.dictation.mapper.InventoryMapper;
import com.dictation.mapper.UnlockMapper;
import com.dictation.record.service.DayRecordService;
import com.dictation.user.entity.CreditRecord;
import com.dictation.user.entity.Unlock;
import com.dictation.user.service.UserService;
import com.dictation.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.MessagingException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

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

    @Autowired
    CreditRecordMapper creditRecordMapper;

    @Autowired
    UnlockMapper unlockMapper;

    @Autowired
    InventoryMapper inventoryMapper;

    @Test
    void contextLoads() throws MessagingException, IOException, ParseException {
        QueryWrapper<Unlock> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",10124);
        unlockMapper.delete(queryWrapper);


//        QueryWrapper<CreditRecord> wrapper = new QueryWrapper<>();
//        wrapper.eq("user_id",10124).eq("reason","解锁单元");
//        creditRecordMapper.delete(wrapper);



//        System.out.println(inventoryMapper.selectList(null));


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


}
