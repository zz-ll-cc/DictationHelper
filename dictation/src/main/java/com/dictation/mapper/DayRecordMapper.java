package com.dictation.mapper;

import com.dictation.record.entity.DayRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DayRecordMapper {

    public int insert(DayRecord dayRecord);
    public int delete(DayRecord dayRecord);
    public int update(DayRecord dayRecord);
    public DayRecord findById(@Param("drid") int drid);
    public DayRecord findByUidAndDate(@Param("uid") int uid,@Param("date") Date date);
    public List<DayRecord> findByUid(@Param("uid") int uid);


}
