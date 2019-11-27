package com.example.dictationprj;

import java.io.Serializable;

public class Word implements Serializable {
    private int id;
    private String chinese;
    private String english;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", chinese='" + chinese + '\'' +
                ", english='" + english + '\'' +
                '}';
    }
}
