package com.dictation.record.controller;

import com.dictation.record.entity.DayRecord;
import com.dictation.record.entity.MonthRecord;
import com.dictation.record.entity.Record;
import com.dictation.record.service.DayRecordService;
import com.dictation.record.service.MonthRecordService;
import com.dictation.record.service.RecordService;
import org.jfree.data.time.Day;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    @Autowired
    MonthRecordService monthRecordService;


    @RequestMapping("/save")
    public void save(@RequestParam("error") int error , @RequestParam("right") int right , @RequestParam("sum") int sum ,
                       @RequestParam("time") String datestr1 , @RequestParam("uid") int uid){
        double acc=(double) right/sum*100;
        Record record=new Record().setRightSum(right).setErrorSum(error).setSum(sum).setAccuracy(acc).setUserId(uid);
        //保存记录
        recordService.save(record);
        DayRecord dayRecord=new DayRecord().setRightSum(right).setErrorSum(error).setSum(sum).setAccuracy(acc).setUserId(uid);
        //保存天记录
        dayRecordService.saveOne(dayRecord);
        String datestr= datestr1.split("-")[0]+"-"+datestr1.split("-")[1]; // yyyy-MM-dd
        MonthRecord monthRecord=new MonthRecord().setErrorSum(error).setUserId(uid).setRightSum(right).setSum(sum).setAccuracy(acc).setDate(datestr);
        //保存月记录
        monthRecordService.saveOne(monthRecord);

    }

    @RequestMapping("/getMaxScore")
    public Record getMaxScore(@RequestParam("uid") int uid,@RequestParam("date") String date){
        return recordService.getMaxScore(uid,date);
    }


}
