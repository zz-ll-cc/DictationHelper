package com.demo.record;

import com.demo.common.model.TblRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RecordService {
    private TblRecord dao=new TblRecord().dao();

    public boolean saveRecord(TblRecord record){
       return record.save();
    }
    public List<TblRecord> getData(){
        List<TblRecord> records=dao.findAll();
        return records;
    }
    public List<Double> getArr(){
        List<Double> accs=new ArrayList<>();
        List<TblRecord> records=dao.findAll();
        for (TblRecord r:records){
            System.out.println(r);
            double acc=r.getAcc();
            System.out.println(acc);
            accs.add(acc);
        }
        System.out.println("arrs:"+accs);
        return accs;
    }
    public List<String> getDate(){
        try{
        List<String> dates=new ArrayList<>();
        List<TblRecord> records=dao.findAll();
        for (TblRecord r:records){
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
}
