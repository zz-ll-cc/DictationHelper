package com.dictation.record.service;

import com.dictation.record.entity.MonthRecord;
import java.util.List;

/**
 * @ClassName MonthRecordService
 * @Description
 * @Author liuzhe
 * @Date 2020-04-14 22:23
 */
public interface MonthRecordService {
    //更新用户每月的记录，如果没有则插入
    MonthRecord saveOne(MonthRecord monthRecord);

    List<MonthRecord> findByUid(int uid);

    //获取近五个月记录
    List<MonthRecord> findLastFiveRecordsByUid(int uid);

    List<Double> findAccuracyListByUid(int uid);

    List<String> findDateListByUid(int uid);
}
