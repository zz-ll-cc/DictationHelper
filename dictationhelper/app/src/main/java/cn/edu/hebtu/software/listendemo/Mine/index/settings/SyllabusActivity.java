package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.credit.Utils.Utils;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarAttr;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarDate;
import cn.edu.hebtu.software.listendemo.credit.component.MonthPager;
import cn.edu.hebtu.software.listendemo.credit.interf.CalendarViewAdapter;
import cn.edu.hebtu.software.listendemo.credit.interf.OnSelectDateListener;
import cn.edu.hebtu.software.listendemo.credit.task.TaskAdapter;
import cn.edu.hebtu.software.listendemo.credit.view.Calendar;
import cn.edu.hebtu.software.listendemo.credit.view.CustomDayView;

/**
 * Created by ldf on 16/11/4.
 */

@SuppressLint("SetTextI18n")
public class SyllabusActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvYear;
    private TextView tvMonth;
    private TextView backToday;
    private CoordinatorLayout content;
    private MonthPager monthPager;
    private RecyclerView rvToDoList;
    private TextView scrollSwitch;
    //  TextView themeSwitch;
    private TextView nextMonthBtn;
    private TextView lastMonthBtn;
    private ImageView ivSignRemind;
    private TextView tvSignDay;
    private int signRemindStatus = 0;
    private List<Map<String, String>> title = new ArrayList<>();
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context context;
    private CalendarDate currentDate;
    private boolean initiated = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// HH:mm:ss
    private SimpleDateFormat dateFormat1 = new SimpleDateFormat("HH:mm");// HH:mm
    private long studyTime = 0;
    private long time1 = 0;
    private long time2 = 0;
    private String[] task;
    private String[] task_name = {"今日累计", "今日最好成绩"};
    private String[] task_content = new String[]{};
    private String[] add_credit = {"+5积分", "+5积分"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        context = this;
        findView();
        ivSignRemind.setOnClickListener(this);
        rvToDoList.setHasFixedSize(true);
        initRecycleView();
        initCurrentDate();
        initCalendarView();
        initToolbarClickListener();
        Log.e("ldf", "OnCreated");
//        Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
//        calendarAdapter.switchToMonth();
//        Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
//        calendarAdapter.switchToWeek(monthPager.getRowIndex());
    }

    public void findView() {
        content = (CoordinatorLayout) findViewById(R.id.content);
        monthPager = (MonthPager) findViewById(R.id.calendar_view);
        //此处强行setViewHeight，毕竟你知道你的日历牌的高度
        monthPager.setViewHeight(Utils.dpi2px(context, 270));
        tvYear = (TextView) findViewById(R.id.show_year_view);
        tvMonth = (TextView) findViewById(R.id.show_month_view);
        backToday = (TextView) findViewById(R.id.back_today_button);
        scrollSwitch = (TextView) findViewById(R.id.scroll_switch);
//      themeSwitch = (TextView) findViewById(R.id.theme_switch);
        nextMonthBtn = (TextView) findViewById(R.id.next_month);
        lastMonthBtn = (TextView) findViewById(R.id.last_month);
        rvToDoList = (RecyclerView) findViewById(R.id.list);
        ivSignRemind = (ImageView) findViewById(R.id.iv_my_credit_sign_remind);
        tvSignDay = (TextView) findViewById(R.id.tv_sign_day);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //每日签到提醒
            case R.id.iv_my_credit_sign_remind:
                if (signRemindStatus == 0) {
                    ivSignRemind.setImageDrawable(getResources().getDrawable(R.drawable.sign_remind_check));
                    signRemindStatus = 1;
                } else {
                    ivSignRemind.setImageDrawable(getResources().getDrawable(R.drawable.sign_remind));
                    signRemindStatus = 0;
                }
                break;
        }
    }

    //初始化任务列表
    public void initRecycleView() {
        rvToDoList.setLayoutManager(new LinearLayoutManager(this));//recycleView线性显示
        java.util.Calendar beginCal = java.util.Calendar.getInstance();
        beginCal.add(java.util.Calendar.HOUR_OF_DAY, -1);
        java.util.Calendar endCal = java.util.Calendar.getInstance();
        long startTime = beginCal.getTimeInMillis();
        long endTime = endCal.getTimeInMillis();
        ArrayList<UsageEvents.Event> mEventList = getEventList(this, startTime, endTime);
        if (mEventList.size() > 0) {
            Log.e("mEventList", "not NULL");
        } else {
            Log.e("mEventList", "NULL");
            task = new String[]{"学习10分钟", "每日听写"};
            task_content = new String[]{"0分钟", "100分"};
        }
        for (int i = 0; i < task.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("task", task[i]);
            map.put("task_name", task_name[i]);
            map.put("task_content", task_content[i]);
            map.put("add_credit", add_credit[i]);
            title.add(map);
        }
        rvToDoList.setAdapter(new TaskAdapter(this, title));
    }

    //初始化currentDate
    private void initCurrentDate() {
        currentDate = new CalendarDate();
        tvYear.setText(currentDate.getYear() + "年");
        tvMonth.setText(currentDate.getMonth() + "");
    }

    //初始化CustomDayView，并作为CalendarViewAdapter的参数传入
    private void initCalendarView() {
        initListener();
        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                context,
                onSelectDateListener,
                CalendarAttr.WeekArrayType.Monday,
                customDayView);
        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                rvToDoList.scrollToPosition(0);
            }
        });
        initMarkData();
        initMonthPager();
    }

    //初始化标记数据，HashMap的形式，可自定义如果存在异步的话，在使用setMarkData之后调用 calendarAdapter.notifyDataChanged();
    private void initMarkData() {
        HashMap<String, String> markData = new HashMap<>();
        //1表示未签到   0表示已签到
        markData.put("2020-4-27", "0");
        markData.put("2020-4-26", "1");
        markData.put("2020-4-25", "0");
        markData.put("2020-4-24", "0");
        markData.put("2020-4-23", "1");
        markData.put("2020-4-22", "0");
        markData.put("2020-4-21", "0");
        markData.put("2020-4-20", "0");
        markData.put("2020-4-19", "0");
        calendarAdapter.setMarkData(markData);
        calendarAdapter.notifyDataChanged();
        calendarAdapter.notifyDataChanged();
    }

    //初始化monthPager，MonthPager继承自ViewPager
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    tvYear.setText(date.getYear() + "年");
                    tvMonth.setText(date.getMonth() + "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //初始化对应功能的listener
    private void initToolbarClickListener() {
        backToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBackToDayBtn();
            }
        });
        scrollSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendarAdapter.getCalendarType() == CalendarAttr.CalendarType.WEEK) {
                    Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
                    calendarAdapter.switchToMonth();
                } else {
                    Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
                    calendarAdapter.switchToWeek(monthPager.getRowIndex());
                }
            }
        });
//        themeSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                refreshSelectBackground();
//            }
//        });
        nextMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
            }
        });
        lastMonthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
            }
        });
    }

    private void initListener() {
        //日期被点击
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                refreshClickDate(date);
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
                monthPager.selectOtherMonth(offset);
            }
        };
    }

    //onWindowFocusChanged回调时，将当前月的种子日期修改为今天
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !initiated) {
            refreshMonthPager();
            initiated = true;
        }
    }

    //显示周日历
    @Override
    protected void onResume() {
//        Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
//        calendarAdapter.switchToWeek(monthPager.getRowIndex());
//        refreshMonthPager();
        super.onResume();
    }


    private void refreshClickDate(CalendarDate date) {
//        Log.e("tt",date+"");
        currentDate = date;
        tvYear.setText(date.getYear() + "年");
        tvMonth.setText(date.getMonth() + "");
    }



    public void onClickBackToDayBtn() { refreshMonthPager(); }

    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged(today);
        tvYear.setText(today.getYear() + "年");
        tvMonth.setText(today.getMonth() + "");
    }

//    private void refreshSelectBackground() {
//        ThemeDayView themeDayView = new ThemeDayView(context, R.layout.custom_day_focus);
//        calendarAdapter.setCustomDayRenderer(themeDayView);
//        calendarAdapter.notifyDataSetChanged();
//        calendarAdapter.notifyDataChanged(new CalendarDate());
//    }

    @SuppressWarnings("ResourceType")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<UsageEvents.Event> getEventList(Context context, long startTime, long endTime) {

        ArrayList<UsageEvents.Event> mEventList = new ArrayList<>();

        Log.e("TAG", " EventUtils-getEventList()   Range start:" + startTime);
        Log.e("TAG", " EventUtils-getEventList()   Range end:" + endTime);
        Log.e("TAG", " EventUtils-getEventList()   Range start:" + dateFormat.format(startTime));
        Log.e("TAG", " EventUtils-getEventList()   Range end:" + dateFormat.format(endTime));
        if (context != null) {
            // if (context.getSystemService("usagestats") != null) {
            UsageStatsManager mUsmManager = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
            UsageEvents events = mUsmManager.queryEvents(startTime, endTime);
            while (events.hasNextEvent()) {
                UsageEvents.Event e = new UsageEvents.Event();
                events.getNextEvent(e);
                if (e != null && (e.getEventType() == 1 || e.getEventType() == 2)) {
                    //Log.e("TAG", " EventUtils-getEventList()  " + e.getTimeStamp() + "   event:" + e.getClassName() + "   type = " + e.getEventType());
                    if (e.getClassName().equals("cn.edu.hebtu.software.listendemo.Host.learnWord.LearnWordActivity")) {
                        Log.e("TAG", " EventUtils-getEventList()  " + dateFormat.format(e.getTimeStamp()) + "   event:" + e.getClassName() + "   type = " + e.getEventType());
                        if (e.getEventType() == 1) {
                            time1 = e.getTimeStamp();
                        } else {
                            time2 = e.getTimeStamp();
                            studyTime = studyTime + (time2 - time1);
                            String time2Str = dateFormat.format(time2);
                            String time1Str = dateFormat.format(time1);
                            String studyTimeStr = dateFormat.format(studyTime);
                            try {
                                Date begin = dateFormat.parse(time1Str);
                                Date end = dateFormat.parse(time2Str);
                                long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒
                                long day1 = between / (24 * 3600);
                                long hour1 = between % (24 * 3600) / 3600;
                                long minute1 = between % 3600 / 60;
                                long second1 = between % 60 / 60;
                                Log.e("TAG", "study" + day1 + "天" + hour1 + "小时" + minute1 + "分钟" + second1 + "秒");
                                Date studyTime = dateFormat.parse(studyTimeStr);
                                long studytime = (studyTime.getTime()) / 1000;
                                long day2 = studytime / (24 * 3600);
                                long hour2 = studytime % (24 * 3600) / 3600;
                                long minute2 = studytime % 3600 / 60;
                                long second2 = studytime % 60 / 60;
                                Log.e("TAG", "studytime" + day2 + "天" + hour2 + "小时" + minute2 + "分钟" + second2 + "秒");
                                if (hour2 != 0) {
                                    task = new String[]{"学习10分钟", "每日听写"};
                                    task_content = new String[]{hour2 + "小时" + minute2 + "分钟", "100分"};
                                    Log.e("studytime", hour2 + "小时" + minute2 + "分钟");
                                } else {
                                    task = new String[]{"学习10分钟", "每日听写"};
                                    task_content = new String[]{minute2 + "分钟", "100分"};
                                    Log.e("studytime", minute2 + "分钟");
                                }
                                mEventList.add(e);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
//                                Log.e("TAG",  "time2："+time2 +" "+dateFormat.format(time2)+" time1："+time1+" "+dateFormat.format(time1)+" 差："+(time2-time1)+" "+dateFormat.format(time2-time1)+" studyTime " +dateFormat1.format(time2-time1));
//                                Log.e("TAG", " studyTime " + dateFormat1.format(studyTime));
                        }
                    }
                }
            }
            //  }
        }
        return mEventList;
    }
}

