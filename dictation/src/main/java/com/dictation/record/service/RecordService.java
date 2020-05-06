package com.dictation.record.service;

import com.dictation.record.entity.Record;

import java.util.List;

public interface RecordService {

    boolean save(Record record);

    List<Record> findAll();

    List<Double> getArr();

    List<String> getDate();


    Record getMaxScore(int uid, String date);
}
