package com.demo.dayrecord;

import com.demo.common.model.TblDayRecord;
import com.demo.common.model.TblRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DayRecordService {
    private TblDayRecord dao=new TblDayRecord().dao();

    public void saveDayRecord(TblDayRecord tblDayRecord){

        List<TblDayRecord> records=this.findDayRecordByUid(tblDayRecord.getUid());
        System.out.println(records);
        for (TblDayRecord dayRecord:records){
            System.out.println(tblDayRecord.getDate().compareTo(dayRecord.getDate()));
            if (tblDayRecord.getDate().compareTo(dayRecord.getDate())==1){
                tblDayRecord.setSum(tblDayRecord.getSum()+dayRecord.getSum());
                tblDayRecord.setRight(tblDayRecord.getRight()+dayRecord.getRight());
                tblDayRecord.setError(tblDayRecord.getError()+dayRecord.getError());
                dao.deleteById(dayRecord.getDrid());
            }
        }
        tblDayRecord.setAcc((double)tblDayRecord.getRight()/tblDayRecord.getSum()*100);
        tblDayRecord.save();
    }
    public List<TblDayRecord> findDayRecordByUid(int uid){
        return dao.find("select * from tbl_day_record where uid = ?",uid);
    }
    public List<Double> getArr(int uid){
        List<Double> accs=new ArrayList<>();
        List<TblDayRecord> records=findDayRecordByUid(uid);
        for (TblDayRecord r:records){
            System.out.println(r);
            double acc=r.getAcc();
            System.out.println(acc);
            accs.add(acc);
        }
        System.out.println("arrs:"+accs);
        return accs;
    }
    public List<String> getDate(int uid){
        try{
            List<String> dates=new ArrayList<>();
            List<TblDayRecord> records=findDayRecordByUid(uid);
            for (TblDayRecord r:records){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String transDate = sdf.format(r.getDate());
                System.out.println(transDate);
                dates.add(transDate);
                System.out.println(dates);
            }
            return dates;}
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public List<TblDayRecord> findDayRecordDataByUid(int uid){
        List<TblDayRecord> dayRecords=this.findDayRecordByUid(uid);
        if (dayRecords.size()>5){
            List<TblDayRecord> records=new ArrayList<>();
            for (int i=5;i>0;i--){
                records.add(dayRecords.get(dayRecords.size()-i));
            }
            return records;
        }else {
            return dayRecords;
        }
    }
}
