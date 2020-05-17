package cn.edu.hebtu.software.listendemo.Entity;

import java.util.List;

/**
 * 根据时间区分的WordList
 */
public class NewOrWrongTimeWord {
    private String time;        // 添加时间
    private List<Word> words;   // 单词

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "NewOrWrongTimeWord{" +
                "time='" + time + '\'' +
                ", words=" + words +
                '}';
    }
}
