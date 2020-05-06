package cn.edu.hebtu.software.listendemo.Entity;

public class HistroyWord {
    private int id;
    private String chinese;
    private String english;
    private int wid;

    @Override
    public String toString() {
        return "HistroyWord{" +
                "id=" + id +
                ", chinese='" + chinese + '\'' +
                ", english='" + english + '\'' +
                ", wid=" + wid +
                '}';
    }

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

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }
}
