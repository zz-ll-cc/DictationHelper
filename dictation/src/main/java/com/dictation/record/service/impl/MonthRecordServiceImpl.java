package com.dictation.record.service.impl;

import com.dictation.mapper.MonthRecordMapper;
import com.dictation.record.entity.MonthRecord;
import com.dictation.record.service.MonthRecordService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName MonthRecordService
 * @Description
 * @Author liuzhe
 * @Date 2020-04-14 22:22
 */
@Service("monthRecordService")
public class MonthRecordServiceImpl implements MonthRecordService {
    @Autowired
    MonthRecordMapper monthRecordMapper;

    @Override
    public MonthRecord saveOne(MonthRecord monthRecord) {
        MonthRecord oldRecord = null;
        if((oldRecord = monthRecordMapper.findByUidAndDate(monthRecord.getUid(),monthRecord.getDate())) != null){
            oldRecord.setSum(monthRecord.getSum() + oldRecord.getSum());
            oldRecord.setMright(monthRecord.getMright() + oldRecord.getMright());
            oldRecord.setError(monthRecord.getError() + oldRecord.getError());
            oldRecord.setAcc(monthRecord.getMright()*1.0/monthRecord.getSum()*100);
            monthRecordMapper.update(oldRecord);
            return oldRecord;
        }
        monthRecord.setAcc(monthRecord.getMright()*1.0/monthRecord.getSum()*100);
        monthRecordMapper.insert(monthRecord);
        return monthRecord;
    }

    @Override
    public List<MonthRecord> findByUid(int uid) {
        return monthRecordMapper.findByUid(uid);
    }


    @Override
    public List<MonthRecord> findLastFiveRecordsByUid(int uid) {
        PageHelper.startPage(1,5);
        return monthRecordMapper.findByUid(uid);
    }

    @Override
    public List<Double> findAccuracyListByUid(int uid) {
        List<Double> accuracyList = null;
        List<MonthRecord> records = monthRecordMapper.findByUid(uid);
        if(records != null && records.size() != 0){
            accuracyList = new ArrayList<>(records.size());
            for(MonthRecord mr : records){
                accuracyList.add(mr.getAcc());
            }
        }
        return accuracyList;
    }

    @Override
    public List<String> findDateListByUid(int uid) {
        List<String> dateList = null;
        List<MonthRecord> records = monthRecordMapper.findByUid(uid);
        if(records != null && records.size() != 0){
            dateList = new ArrayList<>(records.size());
            for (MonthRecord r:records){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String transDate = sdf.format(r.getDate());
                dateList.add(transDate);
            }
        }
        return dateList;
    }
}
