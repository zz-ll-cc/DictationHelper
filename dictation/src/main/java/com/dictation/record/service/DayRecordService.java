package com.dictation.record.service;

import com.dictation.record.entity.DayRecord;

import java.util.List;

public interface DayRecordService {

    //更新用户当天的记录，如果没有则插入
    DayRecord saveOne(DayRecord dayRecord);

    List<DayRecord> findByUid(int uid);

    //获取最近x天内的记录
    List<DayRecord> findByUidAndPaging(int uid,int pageNum,int pageSize);

    //获取最近5条记录
    List<DayRecord> findLastFiveRecordsByUid(int uid);

    List<Double> findAccuracyListByUid(int uid);
    List<String> findDateListByUid(int uid);




}
