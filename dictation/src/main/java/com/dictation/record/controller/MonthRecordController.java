package com.dictation.record.controller;

import com.dictation.record.entity.MonthRecord;
import com.dictation.record.entity.Record;
import com.dictation.record.service.MonthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName MonthRecordController
 * @Description
 * @Author liuzhe
 * @Date 2020-04-14 22:23
 */
@RestController
@RequestMapping("/monthrecord")
public class MonthRecordController {

    @Autowired
    MonthRecordService monthRecordService;

    @RequestMapping("/getdata")
    public List<MonthRecord> getdata(@RequestParam("uid") int uid){
        return monthRecordService.findByUid(uid);
    }

}
