package cn.edu.hebtu.software.listendemo.Mine.index.credit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Entity.UserSignIn;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import cn.edu.hebtu.software.listendemo.credit.Utils.Utils;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarAttr;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarDate;
import cn.edu.hebtu.software.listendemo.credit.component.MonthPager;
import cn.edu.hebtu.software.listendemo.credit.interf.OnSelectDateListener;
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

import static cn.edu.hebtu.software.listendemo.Untils.Constant.USER_KEEP_KEY;


@SuppressLint("SetTextI18n")
public class SignActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnBack;
    private TextView tvYear;
    private TextView tvMonth;
    private LinearLayout llOut;
    private CoordinatorLayout content;
    private MonthPager monthPager;
    private LinearLayout nextMonthBtn;
    private LinearLayout lastMonthBtn;
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();
    private CalendarViewAdapter calendarAdapter;
    private OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private Context context;
    private CalendarDate currentDate;
    private boolean initiated = false;
    private HashMap<String, String> markData = null;
    private SharedPreferences sp;
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private User user;
    private TextView tvSignDayContinue;
    private TextView tvSignDaySum;
    private ThemeDayView themeDayView;
    private static final int GET_SIGN_DAY_CONTINUE = 100;
    private static final int GET_SIGN_DAY_SUM = 200;
    private static final int GET_MARKER_DATE = 600;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_SIGN_DAY_CONTINUE:
                    tvSignDayContinue.setText(msg.obj + "");
                    break;
                case GET_SIGN_DAY_SUM:
                    tvSignDaySum.setText(msg.obj + "");
                    break;
                case GET_MARKER_DATE:
                    if (msg.obj != null) {
                        Map<String, String> markData1 = new HashMap<>();
                        UserSignIn userSignIns = gson.fromJson(msg.obj + "", UserSignIn.class);
                        CalendarDate date = new CalendarDate();
                        markData1 = userSignIns.getYearRecord().get(date.getYear() + "");
                        if (markData1 != null) {
                            for (int i = 0; i < markData1.size(); i++) {
                                if (markData1.size() != 0) {
                                    Iterator<String> iter = markData1.keySet().iterator();
                                    while (iter.hasNext()) {
                                        String key = iter.next();
                                        String[] str = key.split("-");
                                        int month = Integer.parseInt(str[1]);
                                        int day = Integer.parseInt(str[2]);
                                        String d = str[0] + "-" + month + "-" + day;
                                        String value = markData1.get(key);
                                        if (markData == null)
                                            markData = new HashMap<>();
                                        if (value.equals("false")) {
                                            markData.put(d, "1");//1表示未签到   0表示已签到
                                        } else {
                                            markData.put(d, "0");//1表示未签到   0表示已签到
                                        }
                                    }
                                }
                            }
                        }
                    }
                    initCalendarView();
                    calendarAdapter.setMarkData(markData);
                    calendarAdapter.notifyDataChanged();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.green);
        marginTopStateBar();
        context = this;
        findView();
        sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        user = gson.fromJson(sp.getString(USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        List<UnLock> unLocks = null;
        try {
            JSONObject jsonObject = new JSONObject(sp.getString(USER_KEEP_KEY, ""));
            String unLockList = jsonObject.get("unlockList").toString();
            Type type = new TypeToken<List<UnLock>>() {
            }.getType();
            unLocks = new Gson().fromJson(unLockList, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user.setUnLockList(unLocks);
        initMarkData();
        btnBack.setOnClickListener(this);
        initCurrentDate();
        initToolbarClickListener();
    }

    private void marginTopStateBar() {
        llOut = findViewById(R.id.ll_sign_out);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = StatusBarUtil.getStatusBarHeight(this);
        llOut.setLayoutParams(layoutParams);
    }

    public void findView() {
        btnBack = (ImageView) findViewById(R.id.iv_sign_exit);
        content = (CoordinatorLayout) findViewById(R.id.content);
        monthPager = (MonthPager) findViewById(R.id.calendar_view);
        //此处强行setViewHeight，毕竟你知道你的日历牌的高度
        monthPager.setViewHeight(Utils.dpi2px(context, 220));
        tvYear = (TextView) findViewById(R.id.show_year_view);
        tvMonth = (TextView) findViewById(R.id.show_month_view);
        nextMonthBtn = (LinearLayout) findViewById(R.id.next_month);
        lastMonthBtn = (LinearLayout) findViewById(R.id.last_month);
        tvSignDayContinue = (TextView) findViewById(R.id.tv_sign_day_continue);
        tvSignDaySum = (TextView) findViewById(R.id.tv_sign_day_sum);
        themeDayView = new ThemeDayView(context, R.layout.custom_day);
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
            case R.id.iv_sign_exit:
                finish();
                Intent intent1 = new Intent(context, SyllabusActivity.class);
                startActivity(intent1);
                break;
        }
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
        CustomDayView customDayView = new CustomDayView(context, R.layout.custom_day, markData);
        calendarAdapter = new CalendarViewAdapter(context, onSelectDateListener, CalendarAttr.WeekArrayType.Monday, customDayView);
//        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
//            @Override
//            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
//                rvToDoList.scrollToPosition(0);
//            }
//        });
//        initMarkData();
        initMonthPager();
    }

    //初始化标记数据，HashMap的形式，可自定义如果存在异步的话，在使用setMarkData之后调用 calendarAdapter.notifyDataChanged();
    private void initMarkData() {
        getMarkData(user.getUid());
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
                String signDate = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                //签到框
                //创建并显示自定义的dialog
                CustomDialogSign dialog = new CustomDialogSign(getSupportFragmentManager(),user, themeDayView, date, markData, SignActivity.this, sp, calendarAdapter, tvSignDayContinue, tvSignDaySum, monthPager);
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), "sign");
                //calendarAdapter.notifyDataChanged();
                //refreshClickDate(date);
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
        monthPager.requestLayout();
        if (hasFocus && !initiated) {
            initiated = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.green);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //获取签到信息
    private void getMarkData(int userId) {
        OkHttpClient okHttpClient = new OkHttpClient();
        CalendarDate date = new CalendarDate();
        FormBody fb = new FormBody.Builder().add("id", userId + "").add("year", date.getYear() + "").build();
        Request request = new Request.Builder().url(Constant.URL_GET_SIGNDAY).post(fb).build();
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
                Message message0 = new Message();
                message0.what = GET_MARKER_DATE;
                message0.obj = json;
                handler.sendMessage(message0);
                Message message = new Message();
                message.what = GET_SIGN_DAY_CONTINUE;
                int i1 = 0;
                if (user.getContinuousSignIn() != null) {
                    i1 = user.getContinuousSignIn();
                }
                message.obj = i1 + "天";
                handler.sendMessage(message);
                Message message1 = new Message();
                message1.what = GET_SIGN_DAY_SUM;
                int i2 = 0;
                if (user.getAccumulateSignIn() != null) {
                    i2 = user.getAccumulateSignIn();
                }
                message1.obj = i2 + "天";
                handler.sendMessage(message1);

            }
        });
    }


}

