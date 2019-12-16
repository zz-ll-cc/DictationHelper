package com.demo.dayrecord;

import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;

public class DayRecordController extends Controller {
    @Inject
    private DayRecordService dayRecordService;

    public void getdata(){
        int uid=getInt("uid");
        renderJson(dayRecordService.findDayRecordDataByUid(uid));
    }
}
