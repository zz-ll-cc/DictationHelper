package com.dictation.record.service.impl;

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
    public Record save(Record record) {
        recordMapper.insert(record);
        return record;
    }

    @Override
    public List<Record> findAll() {
        return recordMapper.findAll();
    }

    @Override
    public List<Double> getArr() {
        List<Double> accs = null;
        List<Record> records = recordMapper.findAll();
        if(records != null && records.size() != 0){
            accs = new ArrayList<>(records.size());
            for (Record r:records){
                double acc=r.getAcc();
                accs.add(acc);
            }
        }
        return accs;
    }

    @Override
    public List<String> getDate() {
        List<String> dates = null;
        List<Record> records = recordMapper.findAll();
        if(records != null && records.size() != 0){
            dates = new ArrayList<>(records.size());
            for (Record r:records){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String transDate = sdf.format(r.getDate());
                dates.add(transDate);
            }
        }
        return dates;
    }
}
