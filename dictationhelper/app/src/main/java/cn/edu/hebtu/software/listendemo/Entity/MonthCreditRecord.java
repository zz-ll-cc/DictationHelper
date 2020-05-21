package cn.edu.hebtu.software.listendemo.Entity;

import java.util.List;

public class MonthCreditRecord {
    private String recordTime;
    private List<CreditRecord> creditRecords;

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public List<CreditRecord> getCreditRecords() {
        return creditRecords;
    }

    public void setCreditRecords(List<CreditRecord> creditRecords) {
        this.creditRecords = creditRecords;
    }
}
