package cn.edu.hebtu.software.listendemo.Entity;


import java.util.List;
import java.util.Map;


public class UserSignIn {

    private User user;

    //Map<yyyy, Map<yyyy-MM-dd,0/1>>
    private Map<String, Map<String, String>> yearRecord;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<String, Map<String, String>> getYearRecord() {
        return yearRecord;
    }

    public void setYearRecord(Map<String, Map<String, String>> yearRecord) {
        this.yearRecord = yearRecord;
    }

    @Override
    public String toString() {
        return "UserSignIn{" +
                "user=" + user +
                ", yearRecord=" + yearRecord +
                '}';
    }
}
