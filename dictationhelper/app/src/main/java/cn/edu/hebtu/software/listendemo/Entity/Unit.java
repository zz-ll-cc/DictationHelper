package cn.edu.hebtu.software.listendemo.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Unit implements Serializable {
    private List<Word> words = new ArrayList<>();
    private int unid;
    private int bid;
    private int type;
    private String unName;
    private String unTitle;

    @Override
    public String toString() {
        return "Unit{" +
                "words=" + words +
                ", unid=" + unid +
                ", bid=" + bid +
                ", type=" + type +
                ", unName='" + unName + '\'' +
                ", unTitle='" + unTitle + '\'' +
                '}';
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public int getUnid() {
        return unid;
    }

    public void setUnid(int unid) {
        this.unid = unid;
    }

    public String getUnName() {
        return unName;
    }

    public void setUnName(String unName) {
        this.unName = unName;
    }

    public String getUnTitle() {
        return unTitle;
    }

    public void setUnTitle(String unTitle) {
        this.unTitle = unTitle;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
