package com.dictation.mapper;

import com.dictation.record.entity.MonthRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MonthRecordMapper {

    public int insert(MonthRecord monthRecord);
    public int delete(MonthRecord monthRecord);
    public int update(MonthRecord monthRecord);
    public MonthRecord  findById(@Param("mrid") int drid);
    public MonthRecord findByUidAndDate(@Param("uid") int uid, @Param("date") String date);
    public List<MonthRecord> findByUid(@Param("uid") int uid);


}
