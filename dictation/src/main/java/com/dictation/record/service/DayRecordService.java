package com.dictation.record.service;

import com.dictation.record.entity.DayRecord;

import java.util.List;

public interface DayRecordService {

    //更新用户当天的记录，如果没有则插入
    public DayRecord saveOne(DayRecord dayRecord);

    public List<DayRecord> findByUid(int uid);
    //获取最近x天内的记录
    public List<DayRecord> findByUidAndPaging(int uid,int pageNum,int pageSize);
    //获取最近5条记录
    public List<DayRecord> findLastFiveRecordsByUid(int uid);

    public List<Double> findAccuracyListByUid(int uid);
    public List<String> findDateListByUid(int uid);




}
