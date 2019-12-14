package com.demo.record;

import com.demo.common.model.TblDayRecord;
import com.demo.common.model.TblRecord;
import com.demo.dayrecord.DayRecordService;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

import java.util.Date;

public class RecordController extends Controller {
    @Inject
    private RecordService recordService;
    @Inject
    private DayRecordService dayRecordService;

    public void save(){
        int error=getInt("error");
        int right=getInt("right");
        int sum=getInt("sum");
        Date date=getDate("time");
        int uid=getInt("uid");
        double acc=(double) right/sum*100;
        System.out.println(acc);
        TblRecord record=new TblRecord();
        record.set("error",error).set("sum",sum).set("right",right).set("uid",uid)
                .set("date",date).set("acc",acc);
        TblDayRecord dayRecord=new TblDayRecord();
        dayRecord.set("error",error).set("sum",sum).set("right",right).set("uid",uid)
                .set("date",date).set("acc",acc);
        dayRecordService.saveDayRecord(dayRecord);
        renderJson(recordService.saveRecord(record));
    }
}
