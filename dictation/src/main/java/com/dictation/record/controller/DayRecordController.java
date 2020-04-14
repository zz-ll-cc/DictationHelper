package com.dictation.record.controller;

import com.dictation.record.entity.DayRecord;
import com.dictation.record.service.DayRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName DayRecordController
 * @Description
 * @Author zlc
 * @Date 2020-04-14 13:33
 */
@RestController
@RequestMapping("/dayrecord")
public class DayRecordController {

    @Autowired
    DayRecordService dayRecordService;

    @RequestMapping("/getdata")
    public List<DayRecord> getdata(@RequestParam("uid") int uid){
        return dayRecordService.findByUid(uid);
    }




}
