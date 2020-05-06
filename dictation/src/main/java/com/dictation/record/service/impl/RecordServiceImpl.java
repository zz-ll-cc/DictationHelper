package com.dictation.record.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dictation.mapper.RecordMapper;
import com.dictation.record.entity.Record;
import com.dictation.record.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName RecordServiceImpl
 * @Description
 * @Author zlc
 * @Date 2020-04-14 13:17
 */
@Service("recordService")
public class RecordServiceImpl implements RecordService {

    @Autowired
    RecordMapper recordMapper;

    @Override
    public boolean save(Record record) {

        return recordMapper.insert(record) == 1;
    }

    @Override
    public List<Record> findAll() {
        return recordMapper.selectList(null);
    }

    @Override
    public List<Double> getArr() {
        List<Double> accs = null;
        List<Record> records = recordMapper.selectList(null);
        if(records != null && records.size() != 0){
            accs = new ArrayList<>(records.size());
            for (Record r:records){
                double acc = r.getAccuracy();
                accs.add(acc);
            }
        }
        return accs;
    }

    @Override
    public List<String> getDate() {
        List<String> dates = null;
        List<Record> records = recordMapper.selectList(null);
        if(records != null && records.size() != 0){
            dates = new ArrayList<>(records.size());
            for (Record r:records){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String transDate = sdf.format(r.getCreateTime());
                dates.add(transDate);
            }
        }
        return dates;
    }
    @Override
    public Record getMaxScore(int uid, String date) {
        int score=0;
        Record record=null;
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("user_id",uid).eq("create_time",date).orderByAsc("accuracy");
        List<Record> records=recordMapper.selectList(recordQueryWrapper);
        if(records.size()>0){
//            String s=records.get(0).getAccuracy()+"";
//            System.out.println(s);
//            String str=s.substring(0, s.indexOf("."));
            record=records.get(0);
        }
        return record;
    }
}
