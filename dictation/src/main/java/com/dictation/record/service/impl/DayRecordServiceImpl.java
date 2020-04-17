package com.dictation.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dictation.mapper.DayRecordMapper;
import com.dictation.record.entity.DayRecord;
import com.dictation.record.service.DayRecordService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
        QueryWrapper<DayRecord> dayRecordQueryWrapper = new QueryWrapper<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        dayRecordQueryWrapper.eq("user_id",dayRecord.getUserId()).eq("create_time",date);
        if((oldRecord = dayRecordMapper.selectOne(dayRecordQueryWrapper)) != null){
            oldRecord.setSum(dayRecord.getSum() + oldRecord.getSum());
            oldRecord.setRightSum(dayRecord.getRightSum() + oldRecord.getRightSum());
            oldRecord.setErrorSum(dayRecord.getErrorSum() + oldRecord.getErrorSum());
            oldRecord.setAccuracy(dayRecord.getRightSum()*1.0/dayRecord.getSum()*100);
            dayRecordMapper.updateById(oldRecord);
            return oldRecord;
        }
        dayRecord.setAccuracy(dayRecord.getRightSum()*1.0/dayRecord.getSum()*100);
        dayRecordMapper.insert(dayRecord);
        return dayRecord;
    }

    @Override
    public List<DayRecord> findByUid(int uid) {
        QueryWrapper<DayRecord> dayRecordQueryWrapper = new QueryWrapper<>();
        dayRecordQueryWrapper.eq("user_id",uid);
        return dayRecordMapper.selectList(dayRecordQueryWrapper);
    }


    @Override
    public List<DayRecord> findByUidAndPaging(int uid, int pageNum, int pageSize) {
        Page<DayRecord> dayRecordPage = new Page<>(pageNum,pageSize);
        QueryWrapper<DayRecord> dayRecordQueryWrapper = new QueryWrapper<>();
        dayRecordQueryWrapper.eq("user_id",uid);
        return dayRecordMapper.selectPage(dayRecordPage,dayRecordQueryWrapper).getRecords();
    }

    @Override
    public List<DayRecord> findLastFiveRecordsByUid(int uid) {
        Page<DayRecord> dayRecordPage = new Page<>(1,5);
        QueryWrapper<DayRecord> dayRecordQueryWrapper = new QueryWrapper<>();
        dayRecordQueryWrapper.eq("user_id",uid);
        return dayRecordMapper.selectPage(dayRecordPage,dayRecordQueryWrapper).getRecords();
    }

    @Override
    public List<Double> findAccuracyListByUid(int uid) {
        List<Double> accuracyList = null;
        QueryWrapper<DayRecord> dayRecordQueryWrapper = new QueryWrapper<>();
        dayRecordQueryWrapper.eq("user_id",uid);
        List<DayRecord> records = dayRecordMapper.selectList(dayRecordQueryWrapper);
        if(records != null && records.size() != 0){
            accuracyList = new ArrayList<>(records.size());
            for(DayRecord dr : records){
                accuracyList.add(dr.getAccuracy());
            }
        }
        return accuracyList;
    }

    @Override
    public List<String> findDateListByUid(int uid) {
        List<String> dateList = null;
        QueryWrapper<DayRecord> dayRecordQueryWrapper = new QueryWrapper<>();
        dayRecordQueryWrapper.eq("user_id",uid);
        List<DayRecord> records = dayRecordMapper.selectList(dayRecordQueryWrapper);
        if(records != null && records.size() != 0){
            dateList = new ArrayList<>(records.size());
            for (DayRecord r:records){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String transDate = sdf.format(r.getCreateTime());
                dateList.add(transDate);
            }
        }
        return dateList;
    }
}
