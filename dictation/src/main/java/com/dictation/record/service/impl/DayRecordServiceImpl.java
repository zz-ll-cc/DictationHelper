package com.dictation.record.service.impl;

import com.dictation.mapper.DayRecordMapper;
import com.dictation.record.entity.DayRecord;
import com.dictation.record.service.DayRecordService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DayRecordServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-04-13 15:05
 */
@Service("dayRecordService")
public class DayRecordServiceImpl implements DayRecordService {

    @Autowired
    DayRecordMapper dayRecordMapper;

    @Override
    public DayRecord saveOne(DayRecord dayRecord) {
        DayRecord oldRecord = null;
        if((oldRecord = dayRecordMapper.findByUidAndDate(dayRecord.getUid(),dayRecord.getDate())) != null){
            oldRecord.setSum(dayRecord.getSum() + oldRecord.getSum());
            oldRecord.setRight(dayRecord.getRight() + oldRecord.getRight());
            oldRecord.setError(dayRecord.getError() + oldRecord.getError());
            oldRecord.setAcc(dayRecord.getRight()*1.0/dayRecord.getSum()*100);
            dayRecordMapper.update(oldRecord);
            return oldRecord;
        }
        dayRecord.setAcc(dayRecord.getRight()*1.0/dayRecord.getSum()*100);
        dayRecordMapper.insert(dayRecord);
        return dayRecord;
    }

    @Override
    public List<DayRecord> findByUid(int uid) {
        return dayRecordMapper.findByUid(uid);
    }


    @Override
    public List<DayRecord> findByUidAndPaging(int uid, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return dayRecordMapper.findByUid(uid);
    }

    @Override
    public List<DayRecord> findLastFiveRecordsByUid(int uid) {
        PageHelper.startPage(1,5);
        return dayRecordMapper.findByUid(uid);
    }

    @Override
    public List<Double> findAccuracyListByUid(int uid) {
        List<Double> accuracyList = null;
        List<DayRecord> records = dayRecordMapper.findByUid(uid);
        if(records != null && records.size() != 0){
            accuracyList = new ArrayList<>(records.size());
            for(DayRecord dr : records){
                accuracyList.add(dr.getAcc());
            }
        }
        return accuracyList;
    }

    @Override
    public List<String> findDateListByUid(int uid) {
        List<String> dateList = null;
        List<DayRecord> records = dayRecordMapper.findByUid(uid);
        if(records != null && records.size() != 0){
            dateList = new ArrayList<>(records.size());
            for (DayRecord r:records){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String transDate = sdf.format(r.getDate());
                dateList.add(transDate);
            }
        }
        return dateList;
    }
}
