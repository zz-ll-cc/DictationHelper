package com.dictation;

import com.dictation.back.entity.Back;
import com.dictation.back.service.BackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DictationApplicationTests {

    @Autowired
    BackService backService;

    @Test
    void contextLoads() {

    }

}
