package com.dictation.mapper;

import com.dictation.record.entity.Record;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RecordMapper {


    public int insert(Record record);
    public int delete(Record record);
    public int update(Record record);
    public List<Record> findAll();
    public Record findById(@Param("rid") int rid);
    public Record findByUidAndDate(@Param("uid") int uid,@Param("date") Date date);
    public List<Record> findByUid(@Param("uid") int uid);


}
