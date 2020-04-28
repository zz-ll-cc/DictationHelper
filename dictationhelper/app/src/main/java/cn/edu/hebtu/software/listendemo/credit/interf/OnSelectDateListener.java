package cn.edu.hebtu.software.listendemo.credit.interf;


import cn.edu.hebtu.software.listendemo.credit.component.CalendarDate;

public interface OnSelectDateListener {

    void onSelectDate(CalendarDate date);

    void onSelectOtherMonth(int offset);//点击其它月份日期
}
