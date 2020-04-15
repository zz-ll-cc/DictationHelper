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

    @Autowired
    MonthRecordService monthRecordService;


    @RequestMapping("/save")
    public void save(@RequestParam("error") int error , @RequestParam("right") int right , @RequestParam("sum") int sum ,
                       @RequestParam("time") String datestr1 , @RequestParam("uid") int uid){
        double acc=(double) right/sum*100;
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat2= new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1=simpleDateFormat1.parse(datestr1);
            Record record=new Record(error,right,date1,uid,sum,acc);
            //保存记录
            recordService.save(record);
            String datestr2= datestr1.split(" ")[0]; // yyyy-MM-dd
            Date date2=simpleDateFormat2.parse(datestr2);
            DayRecord dayRecord=new DayRecord(error,uid,right,sum,date2,acc);
            //保存天记录
            dayRecordService.saveOne(dayRecord);
            String datestr3= datestr1.split("-")[0]+"-"+datestr1.split("-")[1]; // yyyy-MM-dd
            MonthRecord monthRecord=new MonthRecord(error,uid,right,sum,datestr3,acc);
            //保存月记录
            monthRecordService.saveOne(monthRecord);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

  }

}
