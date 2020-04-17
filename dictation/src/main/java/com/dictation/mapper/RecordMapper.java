package com.dictation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dictation.record.entity.Record;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface RecordMapper extends BaseMapper<Record> {


//    public int insert(Record record);
//    public int delete(Record record);
//    public int update(Record record);
//    public List<Record> findAll();
//    public Record findById(@Param("rid") int rid);
//    public Record findByUidAndDate(@Param("uid") int uid,@Param("date") Date date);
//    public List<Record> findByUid(@Param("uid") int uid);


}
