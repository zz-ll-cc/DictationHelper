package com.dictation.record.service;

import com.dictation.record.entity.Record;

import java.util.List;

public interface RecordService {

    public Record save(Record record);

    public List<Record> findAll();

    public List<Double> getArr();

    public List<String> getDate();


}
