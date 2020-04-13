package com.dictation;

import com.dictation.back.entity.Back;
import com.dictation.back.service.BackService;
import com.dictation.book.entity.Book;
import com.dictation.book.service.BookService;
import com.dictation.record.entity.DayRecord;
import com.dictation.record.service.DayRecordService;
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

    @Autowired
    DayRecordService dayRecordService;

    @Test
    void contextLoads() {


    }

}
