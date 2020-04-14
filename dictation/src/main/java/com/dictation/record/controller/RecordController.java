package com.dictation.record.controller;

import com.dictation.record.entity.DayRecord;
import com.dictation.record.entity.Record;
import com.dictation.record.service.DayRecordService;
import com.dictation.record.service.RecordService;
import org.jfree.data.time.Day;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @ClassName RecordController
 * @Description
 * @Author zlc
 * @Date 2020-04-14 13:24
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    RecordService recordService;

    @Autowired
    DayRecordService dayRecordService;


    @RequestMapping("/save")
    public Record save(@RequestParam("error") int error , @RequestParam("right") int right , @RequestParam("sum") int sum ,
                       @RequestParam("time") Date date , @RequestParam("uid") int uid){
        double acc=(double) right/sum*100;
        Record record=new Record(error,right,date,uid,sum,acc);
        DayRecord dayRecord=new DayRecord(error,uid,right,sum,date,acc);
        dayRecordService.saveOne(dayRecord);
        return recordService.save(record);
    }




}
