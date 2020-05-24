package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Record;
import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Entity.UserSignIn;
import cn.edu.hebtu.software.listendemo.Mine.index.credit.CreditDetailActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import cn.edu.hebtu.software.listendemo.credit.Utils.Utils;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarAttr;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarDate;
import cn.edu.hebtu.software.listendemo.credit.component.MonthPager;
import cn.edu.hebtu.software.listendemo.credit.interf.OnSelectDateListener;
import cn.edu.hebtu.software.listendemo.credit.task.TaskAdapter;
import cn.edu.hebtu.software.listendemo.credit.view.Calendar;
import cn.edu.hebtu.software.listendemo.credit.view.CalendarViewAdapter;
import cn.edu.hebtu.software.listendemo.credit.view.CustomDayView;
import cn.edu.hebtu.software.listendemo.credit.view.ThemeDayView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.edu.hebtu.software.listendemo.Untils.Constant.SP_NAME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.USER_KEEP_KEY;


@SuppressLint("SetTextI18n")
public class SyllabusActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvYear;
    private TextView tvMonth;
    private TextView backToday;
    private LinearLayout llOut;
    //    private CoordinatorLayout content;
//    private MonthPager monthPager;
    private RecyclerView rvToDoList;
    //    private TextView scrollSwitch;
    //  TextView themeSwitch;
//    private TextView nextMonthBtn;
//    private TextView lastMonthBtn;
    //    private ImageView ivSignRemind;
//    private TextView tvSignDayContinue;
//    private TextView tvSignDaySum;
    private TextView tvCreditSum;
    private TextView dateTv;
    private TextView tvCreditDetail;
    private TextView tvWordSum;
    private ImageView btnBack;
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
    private long studyMinute = 0;
    private long time1 = 0;
    private long time2 = 0;
    private TaskAdapter taskAdapter;
    private String[] task = {"学习10分钟", "每日听写"};
    private String[] task_name = {"", "今日最好成绩"};
    private String[] task_content = {"", ""};
    private String[] add_credit = {"", "+5积分"};
    private String[] tag = {"false", "false"};
    //    private String[] task = {"学习10分钟", "学习30分钟", "学习60分钟", "每日听写"};
//    private String[] task_name = {"", "", "", "今日最好成绩"};
//    private String[] task_content = {"", "", "", ""};
//    private String[] add_credit = {"+1积分", "+3积分", "+5积分", "+5积分"};
//    String[] tag = {"false", "false", "false", "false"};
    private HashMap<String, String> markData = null;
    private ThemeDayView themeDayView;
    private SharedPreferences sp;
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private User user;
    private static final int GET_SIGN_DAY_CONTINUE = 100;
    private static final int GET_SIGN_DAY_SUM = 200;
    private static final int GET_CREDIT_SUM = 300;
    private static final int GET_WORD_SUM = 400;
    private static final int UPDATE_USER = 500;
    //    private static final int GET_MARKER_DATE = 600;
    private static final int GET_MAX_RECORD = 700;
    private static final int GET_CREDICT_RECORD = 1000;
    private ImageView tvSign;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case GET_SIGN_DAY_CONTINUE:
//                    tvSignDayContinue.setText(msg.obj + "");
//                    break;
//                case GET_SIGN_DAY_SUM:
//                    tvSignDaySum.setText(msg.obj + "");
//                    break;
//                case GET_CREDIT_SUM:
//                    tvCreditSum.setText(msg.obj + "");
//                    break;
//                case GET_MARKER_DATE:
//                    if (msg.obj != null) {
//                        Map<String, String> markData1 = new HashMap<>();
//                        UserSignIn userSignIns = gson.fromJson(msg.obj + "", UserSignIn.class);
//                        CalendarDate date = new CalendarDate();
//                        markData1 = userSignIns.getYearRecord().get(date.getYear() + "");
//                        if (markData1 != null) {
//                            for (int i = 0; i < markData1.size(); i++) {
//                                if (markData1.size() != 0) {
//                                    Iterator<String> iter = markData1.keySet().iterator();
//                                    while (iter.hasNext()) {
//                                        String key = iter.next();
//                                        String[] str = key.split("-");
//                                        int month = Integer.parseInt(str[1]);
//                                        int day = Integer.parseInt(str[2]);
//                                        String d = str[0] + "-" + month + "-" + day;
//                                        String value = markData1.get(key);
//                                        if (markData == null)
//                                            markData = new HashMap<>();
//                                        if (value.equals("false")) {
//                                            markData.put(d, "1");//1表示未签到   0表示已签到
//                                        } else {
//                                            markData.put(d, "0");//1表示未签到   0表示已签到
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    initCalendarView();
//                    Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
//                    calendarAdapter.switchToMonth();
////                    Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
////                    calendarAdapter.switchToWeek(monthPager.getRowIndex());
//                    calendarAdapter.setMarkData(markData);
//                    calendarAdapter.notifyDataChanged();
//                    break;
                case GET_MAX_RECORD:
                    if (!msg.obj.equals("0")) {
                        Record record = gson.fromJson(msg.obj + "", Record.class);
                        int score1 = 0;
                        if (record.getId() != null) {
                            String s = record.getAccuracy() + "";
                            String str = s.substring(0, s.indexOf("."));
                            score1 = Integer.parseInt(str);
                            task_content[1] = score1 + "分";
//                            task_content[3] = score1 + "分";
                        } else {
                            task_content[1] = "未听写";
//                            task_content[3] = "未听写";
                        }
//                        task_content[1] =  score+ "分";
                        for (int i = 0; i < task.length; i++) {
                            Map<String, String> map = new HashMap<>();
                            map.put("task", task[i]);
                            map.put("task_name", task_name[i]);
                            map.put("task_content", task_content[i]);
                            map.put("add_credit", add_credit[i]);
                            map.put("tag", tag[i]);
                            title.add(map);
                        }
                        Log.e("tagg", "1");
                        Log.e("tagg", title.toString());
                        rvToDoList.setHasFixedSize(true);
                        rvToDoList.setLayoutManager(new LinearLayoutManager(SyllabusActivity.this));//recycleView线性显示
                        taskAdapter = new TaskAdapter(SyllabusActivity.this,getSupportFragmentManager(),tag, studyMinute, tvCreditSum, score1, user, SyllabusActivity.this, title, sp);
//                        Log.e("userJson",user.toString()+"");
                        rvToDoList.setAdapter(taskAdapter);
                    } else {
                        task_content[1] = "未听写";
//                        task_content[3] = "未听写";
                        int score = 0;
                        for (int i = 0; i < task.length; i++) {
                            Map<String, String> map = new HashMap<>();
                            map.put("task", task[i]);
                            map.put("task_name", task_name[i]);
                            map.put("task_content", task_content[i]);
                            map.put("add_credit", add_credit[i]);
                            map.put("tag", tag[i]);
                            title.add(map);
                        }
                        Log.e("tagg", "1");
                        Log.e("tagg", title.toString());
                        rvToDoList.setHasFixedSize(true);
                        rvToDoList.setLayoutManager(new LinearLayoutManager(SyllabusActivity.this));//recycleView线性显示
//                        Log.e("userJson",user.toString()+"");
                        taskAdapter = new TaskAdapter(SyllabusActivity.this,getSupportFragmentManager(),tag, studyMinute, tvCreditSum, score, user, SyllabusActivity.this, title, sp);
                        rvToDoList.setAdapter(taskAdapter);
                    }
                    break;
                case GET_CREDICT_RECORD:
                    Type type = new TypeToken<Map<String, String>>() {
                    }.getType();
                    Map<String, String> records = gson.fromJson(msg.obj + "", type);
                    if (title.size() > 0) {
                        if (records.get("学习10分钟").equals("true")) {
                            if (records.get("学习30分钟").equals("true")) {
                                if (records.get("学习60分钟").equals("true")) {
                                    title.get(0).put("tag", "true");
                                } else {
                                    title.get(0).put("tag", "false");
                                }
                                title.get(0).put("task", "学习60分钟");
                                title.get(0).put("add_credit", "+5积分");
                            } else {
                                title.get(0).put("tag", "false");
                                title.get(0).put("task", "学习30分钟");
                                title.get(0).put("add_credit", "+3积分");
                            }
                        } else {
                            title.get(0).put("tag", "false");
                            title.get(0).put("task", "学习10分钟");
                            title.get(0).put("add_credit", "+1积分");
                        }
                    } else {
                        if (records.get("学习10分钟").equals("true")) {
                            if (records.get("学习30分钟").equals("true")) {
                                if (records.get("学习60分钟").equals("true")) {
                                    tag[0] = "true";
                                } else {
                                    tag[0] = "false";
                                }
                                task[0] = "学习60分钟";
                                add_credit[0] = "+5积分";
                            } else {
                                tag[0] = "false";
                                task[0] = "学习30分钟";
                                add_credit[0] = "+3积分";
                            }
                        } else {
                            tag[0] = "false";
                            task[0] = "学习10分钟";
                            add_credit[0] = "+1积分";
                        }
                    }
                    if (records.get("有效听写").equals("true")) {
                        if (title.size() > 0) {
                            title.get(1).put("tag", "true");
                            title.get(1).put("add_credit","已领取");
                        }
                        tag[1] = "true";
                        add_credit[1]="已领取";
                    }
                    if (taskAdapter != null) {
                        taskAdapter.notifyDataSetChanged();
                    }
                    Log.e("tagg", "2");
                    Log.e("tagg", title.toString());
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syllabus);
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.green3);
        marginTopStateBar();
        context = this;
        findView();
//        EventBus.getDefault().register(this);
        sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        user = gson.fromJson(sp.getString(USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
//        Log.e("userJsonSyllabus1",sp.getString(Constant.USER_KEEP_KEY,""));
        List<UnLock> unLocks = null;
        try {
            JSONObject jsonObject = new JSONObject(sp.getString(Constant.USER_KEEP_KEY, ""));
            String unLockList = jsonObject.get("unlockList").toString();
            Type type = new TypeToken<List<UnLock>>() {
            }.getType();
            unLocks = new Gson().fromJson(unLockList, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user.setUnLockList(unLocks);
//        Log.e("userJsonSyllabus2",user.toString());
        getCreditRecord(user.getUid());
//        initMarkData();
//        ivSignRemind.setOnClickListener(this);
        tvCreditDetail.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        initRecycleView();

        int i1 = 0;
        if (user.getContinuousSignIn() != null) {
            i1 = user.getContinuousSignIn();
        }
        int i2 = 0;
        if (user.getAccumulateSignIn() != null) {
            i2 = user.getAccumulateSignIn();
        }
        int i3 = 0;
        if (user.getUserCredit() != null) {
            i3 = user.getUserCredit();
        }
//        tvSignDayContinue.setText(i1+ "天");
//        tvSignDaySum.setText(i2+ "天");
        tvCreditSum.setText(i3+ "分");

//        initCurrentDate();
        //initCalendarView();
//        initToolbarClickListener();
//        Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
//        calendarAdapter.switchToMonth();
//        Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
//        calendarAdapter.switchToWeek(monthPager.getRowIndex());
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateSp(User user0) {
//        sp.edit().putString(USER_KEEP_KEY, gson.toJson(user0)).commit();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }

    private void marginTopStateBar() {
        llOut = findViewById(R.id.ll_syllabus_out);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = StatusBarUtil.getStatusBarHeight(this);
        llOut.setLayoutParams(layoutParams);
    }

    public void findView() {
//        content = (CoordinatorLayout) findViewById(R.id.content);
//        monthPager = (MonthPager) findViewById(R.id.calendar_view);
//        //此处强行setViewHeight，毕竟你知道你的日历牌的高度
//        monthPager.setViewHeight(Utils.dpi2px(context, 270));
        tvYear = (TextView) findViewById(R.id.show_year_view);
        tvMonth = (TextView) findViewById(R.id.show_month_view);
//        backToday = (TextView) findViewById(R.id.back_today_button);
//        scrollSwitch = (TextView) findViewById(R.id.scroll_switch);
//      themeSwitch = (TextView) findViewById(R.id.theme_switch);
//        nextMonthBtn = (TextView) findViewById(R.id.next_month);
//        lastMonthBtn = (TextView) findViewById(R.id.last_month);
        rvToDoList = (RecyclerView) findViewById(R.id.list);
//        ivSignRemind = (ImageView) findViewById(R.id.iv_my_credit_sign_remind);
//        tvSignDayContinue = (TextView) findViewById(R.id.tv_sign_day_continue);
//        tvSignDaySum = (TextView) findViewById(R.id.tv_sign_day_sum);
        themeDayView = new ThemeDayView(context, R.layout.custom_day);
        tvCreditSum = (TextView) findViewById(R.id.tv_sign_point_sum);
        tvCreditDetail = (TextView) findViewById(R.id.tv_my_point_detail);
        tvWordSum = (TextView) findViewById(R.id.tv_word_sum);
        btnBack = (ImageView) findViewById(R.id.iv_my_credit_back);
        dateTv = themeDayView.findViewById(R.id.date);
        tvSign = (ImageView) findViewById(R.id.tv_sign);
        tvSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SyllabusActivity.this, SignActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //每日签到提醒
//            case R.id.iv_my_credit_sign_remind:
//                if (signRemindStatus == 0) {
//                    ivSignRemind.setImageDrawable(getResources().getDrawable(R.drawable.sign_remind_check));
//                    signRemindStatus = 1;
//                } else {
//                    ivSignRemind.setImageDrawable(getResources().getDrawable(R.drawable.sign_remind));
//                    signRemindStatus = 0;
//                }
//                break;
            case R.id.tv_my_point_detail:
                Intent intent = new Intent(this, CreditDetailActivity.class);
                intent.putExtra("userId", user.getUid());
                startActivity(intent);
                break;
            case R.id.iv_my_credit_back:
                finish();
                break;
        }
    }

    //初始化任务列表
    public void initRecycleView() {
        getListenerRecordData(user.getUid());
        Long currentTimestamps = System.currentTimeMillis();
        Long oneDayTimestamps = Long.valueOf(60 * 60 * 24 * 1000);
        long startTime = currentTimestamps - (currentTimestamps + 60 * 60 * 8 * 1000) % oneDayTimestamps;
        //java.util.Calendar beginCal = java.util.Calendar.getInstance();
        //beginCal.add(java.util.Calendar.HOUR_OF_DAY, -1);
        java.util.Calendar endCal = java.util.Calendar.getInstance();
        //long startTime = beginCal.getTimeInMillis();
        long endTime = endCal.getTimeInMillis();
        ArrayList<UsageEvents.Event> mEventList = getEventList(this, startTime, endTime);
        if (mEventList.size() > 0) {
            Log.e("mEventList", "not NULL");
        } else {
            Log.e("mEventList", "NULL");
            tvWordSum.setText("0分钟");
            task[0] = "学习10分钟";
            add_credit[0] = "+1积分";
        }
    }

//    //初始化currentDate
//    private void initCurrentDate() {
//        currentDate = new CalendarDate();
//        tvYear.setText(currentDate.getYear() + "年");
//        tvMonth.setText(currentDate.getMonth() + "");
//    }

//    //初始化CustomDayView，并作为CalendarViewAdapter的参数传入
//    private void initCalendarView() {
//        initListener();
//        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day, markData);
//        calendarAdapter = new CalendarViewAdapter(context, onSelectDateListener, CalendarAttr.WeekArrayType.Monday, customDayView);
//        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
//            @Override
//            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
//                rvToDoList.scrollToPosition(0);
//            }
//        });
////        initMarkData();
//        initMonthPager();
//    }

//    //初始化标记数据，HashMap的形式，可自定义如果存在异步的话，在使用setMarkData之后调用 calendarAdapter.notifyDataChanged();
//    private void initMarkData() {
//        getMarkData(user.getUid());
//    }

//    //初始化monthPager，MonthPager继承自ViewPager
//    private void initMonthPager() {
//        monthPager.setAdapter(calendarAdapter);
//        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
//        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
//            @Override
//            public void transformPage(View page, float position) {
//                position = (float) Math.sqrt(1 - Math.abs(position));
//                page.setAlpha(position);
//            }
//        });
//        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                mCurrentPage = position;
//                currentCalendars = calendarAdapter.getPagers();
//                Log.e("currentCalendars", currentCalendars.size() + "");
//                if (currentCalendars.get(position % currentCalendars.size()) != null) {
//                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
//                    currentDate = date;
//                    tvYear.setText(date.getYear() + "年");
//                    tvMonth.setText(date.getMonth() + "");
//                    Log.e("currentCalendars", date.getMonth() + "");
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
//    }

//    //初始化对应功能的listener
//    private void initToolbarClickListener() {
//        backToday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onClickBackToDayBtn();
//            }
//        });
////        scrollSwitch.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                if (calendarAdapter.getCalendarType() == CalendarAttr.CalendarType.WEEK) {
////                    Utils.scrollTo(content, rvToDoList, monthPager.getViewHeight(), 200);
////                    calendarAdapter.switchToMonth();
////                } else {
////                    Utils.scrollTo(content, rvToDoList, monthPager.getCellHeight(), 200);
////                    calendarAdapter.switchToWeek(monthPager.getRowIndex());
////                }
////            }
////        });
////        themeSwitch.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                refreshSelectBackground();
////            }
////        });
//        nextMonthBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                monthPager.setCurrentItem(monthPager.getCurrentPosition() + 1);
//            }
//        });
//        lastMonthBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                monthPager.setCurrentItem(monthPager.getCurrentPosition() - 1);
//            }
//        });
//    }
//
//    private void initListener() {
//        //日期被点击
//        onSelectDateListener = new OnSelectDateListener() {
//            @Override
//            public void onSelectDate(CalendarDate date) {
////                String signDate = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
////                //签到框
////                //创建并显示自定义的dialog
////                CustomDialogSign dialog = new CustomDialogSign(user, themeDayView, date, markData, SyllabusActivity.this, sp, calendarAdapter, tvCreditSum, tvSignDayContinue, tvSignDaySum, monthPager);
////                dialog.setCancelable(false);
////                dialog.show(getSupportFragmentManager(), "sign");
////                calendarAdapter.notifyDataChanged();
////                refreshClickDate(date);
//            }
//
//            @Override
//            public void onSelectOtherMonth(int offset) {
//                //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
//                monthPager.selectOtherMonth(offset);
//            }
//        };
//    }
//
//    //onWindowFocusChanged回调时，将当前月的种子日期修改为今天
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        monthPager.requestLayout();
//        if (hasFocus && !initiated) {
////            refreshMonthPager();
//            initiated = true;
//        }
//    }

    //显示周日历
    @Override
    protected void onResume() {
//        refreshMonthPager();
        super.onResume();
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.green3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        tvYear.setText(date.getYear() + "年");
        tvMonth.setText(date.getMonth() + "");
    }

    public void onClickBackToDayBtn() {
        refreshMonthPager();
    }

    private void refreshMonthPager() {
        CalendarDate today = new CalendarDate();
        calendarAdapter.notifyDataChanged(today);
        tvYear.setText(today.getYear() + "年");
        tvMonth.setText(today.getMonth() + "");
    }

    public void refreshSelectBackground() {
        ThemeDayView themeDayView = new ThemeDayView(context, R.layout.custom_day_focus);
        calendarAdapter.setCustomDayRenderer(themeDayView);
        calendarAdapter.notifyDataSetChanged();
        calendarAdapter.notifyDataChanged(new CalendarDate());
    }

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
                            String studyTimeStr = dateFormat.format(studyTime);
                            try {
                                Date studyTime = dateFormat.parse(studyTimeStr);
                                long studytime = (studyTime.getTime()) / 1000;
                                long day2 = studytime / (24 * 3600);
                                long hour2 = studytime % (24 * 3600) / 3600;
                                long minute2 = studytime % 3600 / 60;
                                long second2 = studytime % 60 / 60;
                                Log.e("TAG", "studytime" + day2 + "天" + hour2 + "小时" + minute2 + "分钟" + second2 + "秒");
                                if (hour2 != 0) {
//                                    task[0] = "学习60分钟";
//                                    add_credit[0] = "+5积分";
                                    tvWordSum.setText(hour2 + "时" + minute2 + "分");
                                    studyMinute = hour2 * 60 + minute2;
                                    Log.e("studytime", hour2 + "小时" + minute2 + "分钟");
                                } else {
//                                    if (minute2 < 30) {
//                                        task[0] = "学习10分钟";
//                                        add_credit[0] = "+1积分";
//                                    } else if (minute2 > 30 && minute2 < 60) {
//                                        task[0] = "学习30分钟";
//                                        add_credit[0] = "+3积分";
//                                    } else {
//                                        task[0] = "学习60分钟";
//                                        add_credit[0] = "+5积分";
//                                    }
                                    tvWordSum.setText(minute2 + "分钟");
                                    studyMinute = minute2;
                                    Log.e("studytime", minute2 + "分钟");
                                }
                                mEventList.add(e);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
            //  }
        }
        return mEventList;
    }

//    //获取签到信息
//    private void getMarkData(int userId) {
//        OkHttpClient okHttpClient = new OkHttpClient();
//        CalendarDate date = new CalendarDate();
//        FormBody fb = new FormBody.Builder().add("id", userId + "").add("year", date.getYear() + "").build();
//        Request request = new Request.Builder().url(Constant.URL_GET_SIGNDAY).post(fb).build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            /**
//             * 未完待续
//             *
//             * @param call
//             * @param response
//             * @throws IOException
//             */
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String json = response.body().string();
////                Message message0 = new Message();
////                message0.what = GET_MARKER_DATE;
////                message0.obj = json;
////                handler.sendMessage(message0);
//                Message message = new Message();
//                message.what = GET_SIGN_DAY_CONTINUE;
//                int i1 = 0;
//                if (user.getContinuousSignIn() != null) {
//                    i1 = user.getContinuousSignIn();
//                }
//                message.obj = i1 + "天";
//                handler.sendMessage(message);
//                Message message1 = new Message();
//                message1.what = GET_SIGN_DAY_SUM;
//                int i2 = 0;
//                if (user.getAccumulateSignIn() != null) {
//                    i2 = user.getAccumulateSignIn();
//                }
//                message1.obj = i2 + "天";
//                handler.sendMessage(message1);
//                Message message2 = new Message();
//                message2.what = GET_CREDIT_SUM;
//                int i3 = 0;
//                if (user.getUserCredit() != null) {
//                    i3 = user.getUserCredit();
//                }
//                message2.obj = i3 + "分";
//                handler.sendMessage(message2);
//
//            }
//        });
//    }

    //获取当天听写记录最高分
    private void getListenerRecordData(int userId) {
        OkHttpClient okHttpClient = new OkHttpClient();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String datestr = dateFormat.format(calendar.getTime());
        System.out.println(datestr);
        FormBody fb = new FormBody.Builder().add("uid", userId + "").add("date", datestr + "").build();
        Request request = new Request.Builder().url(Constant.URL_GET_MAX_SCORE).post(fb).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            /**
             * 未完待续
             *
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("listenrecord", json);
                if (json == null || json.equals("")) {
                    Message message = new Message();
                    message.what = GET_MAX_RECORD;
                    message.obj = "0";
                    handler.sendMessage(message);
                } else {
                    Message message = new Message();
                    message.what = GET_MAX_RECORD;
                    message.obj = json;
                    handler.sendMessage(message);
                }

            }
        });
    }

    //获取今天领取积分的记录
    public void getCreditRecord(int userId) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("id", userId + "").build();
        Request request = new Request.Builder().url(Constant.URL_CREDIT_RECORD).post(fb).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            /**
             * 未完待续
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("checkCredit", "" + json);
                if (response != null || !response.equals("")) {
                    Message message = new Message();
                    message.what = GET_CREDICT_RECORD;
                    message.obj = json;
                    handler.sendMessage(message);
                }

            }
        });
    }

//    // 更新用户信息
//    private void updateUser(int userId) {
//        OkHttpClient okHttpClient = new OkHttpClient();
//        FormBody fb = new FormBody.Builder().add("id", userId + "").build();
//        Request request = new Request.Builder().url(Constant.URL_UPDATE_MYSELF).post(fb).build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            /**
//             * 未完待续
//             * @param call
//             * @param response
//             * @throws IOException
//             */
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String json = response.body().string();
//                List<UnLock> unLocks = null;
//                try {
//                    JSONObject jsonObject = new JSONObject(json);
//                    String unLockList = jsonObject.get("unlockList").toString();
//                    Type type = new TypeToken<List<UnLock>>(){}.getType();
//                    unLocks = new Gson().fromJson(unLockList,type);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                user = gson.fromJson(json,User.class);
//                user.setUnLockList(unLocks);
//                getSharedPreferences(SP_NAME,MODE_PRIVATE).edit().putString(USER_KEEP_KEY,json).commit();
//            }
//        });
//    }

}

