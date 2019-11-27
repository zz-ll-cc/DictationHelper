package com.example.dictationprj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Unit implements Serializable {
    private List<Word> words = new ArrayList<>();
    private int id;
    private String unitName;
    private String unitTitle;

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }
}
