package com.dictation.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.mapper.MonthRecordMapper;
import com.dictation.record.entity.DayRecord;
import com.dictation.record.entity.MonthRecord;
import com.dictation.record.service.MonthRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        QueryWrapper<MonthRecord> monthRecordQueryWrapper = new QueryWrapper<>();
        monthRecordQueryWrapper.eq("user_id",monthRecord.getUserId()).eq("date",monthRecord.getDate());
        if((oldRecord = monthRecordMapper.selectOne(monthRecordQueryWrapper))!= null){
            oldRecord.setSum(monthRecord.getSum() + oldRecord.getSum());
            oldRecord.setRightSum(monthRecord.getRightSum() + oldRecord.getRightSum());
            oldRecord.setErrorSum(monthRecord.getErrorSum() + oldRecord.getErrorSum());
            oldRecord.setAccuracy(monthRecord.getRightSum()*1.0/monthRecord.getSum()*100);
            monthRecordMapper.updateById(oldRecord);
            return oldRecord;
        }
        monthRecord.setAccuracy(monthRecord.getRightSum()*1.0/monthRecord.getSum()*100);
        monthRecordMapper.insert(monthRecord);
        return monthRecord;
    }

    @Override
    public List<MonthRecord> findByUid(int uid) {
        QueryWrapper<MonthRecord> monthRecordQueryWrapper = new QueryWrapper<>();
        monthRecordQueryWrapper.eq("user_id",uid);
        return monthRecordMapper.selectList(monthRecordQueryWrapper);
    }


    @Override
    public List<MonthRecord> findLastFiveRecordsByUid(int uid,int pageNum,int pageSize) {
        Page<MonthRecord> monthRecordPage = new Page<>(pageNum,pageSize);
        QueryWrapper<MonthRecord> monthRecordQueryWrapper = new QueryWrapper<>();
        monthRecordQueryWrapper.eq("user_id",uid);
        return monthRecordMapper.selectPage(monthRecordPage,monthRecordQueryWrapper).getRecords();
    }

    @Override
    public List<Double> findAccuracyListByUid(int uid) {
        List<Double> accuracyList = null;
        QueryWrapper<MonthRecord> monthRecordQueryWrapper = new QueryWrapper<>();
        monthRecordQueryWrapper.eq("user_id",uid);
        List<MonthRecord> records = monthRecordMapper.selectList(monthRecordQueryWrapper);
        if(records != null && records.size() != 0){
            accuracyList = new ArrayList<>(records.size());
            for(MonthRecord mr : records){
                accuracyList.add(mr.getAccuracy());
            }
        }
        return accuracyList;
    }

    @Override
    public List<String> findDateListByUid(int uid) {
        List<String> dateList = null;
        QueryWrapper<MonthRecord> monthRecordQueryWrapper = new QueryWrapper<>();
        monthRecordQueryWrapper.eq("user_id",uid);
        List<MonthRecord> records = monthRecordMapper.selectList(monthRecordQueryWrapper);
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
