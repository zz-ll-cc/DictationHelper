package cn.edu.hebtu.software.listendemo.credit.component;
//日历显示类
public class CalendarAttr {
    private WeekArrayType weekArrayType;    //以何种方式排列星期:WeekArrayType
    private CalendarType calendarType;      //日历周布局或者月布局:CalendarType} 布局类型
    private int cellHeight;                 //日期格子高度
    private int cellWidth;                  //日期格子宽度

    //星期的排列方式
    public enum WeekArrayType {
        /*周日作为本周的第一天*/Sunday,
        /*周一作为本周的第一天*/Monday
    }

    //日历的周布局或月布局
    public enum CalendarType {
        WEEK, MONTH
    }

    public WeekArrayType getWeekArrayType() {
        return weekArrayType;
    }

    public void setWeekArrayType(WeekArrayType weekArrayType) {
        this.weekArrayType = weekArrayType;
    }

    public CalendarType getCalendarType() {
        return calendarType;
    }

    public void setCalendarType(CalendarType calendarType) {
        this.calendarType = calendarType;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }


}
