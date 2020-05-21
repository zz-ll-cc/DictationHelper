package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Host.listenWord.ListenResultSelectActivity;
import cn.edu.hebtu.software.listendemo.QiniuUtils.Json;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.credit.Utils.Utils;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarDate;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarRenderer;
import cn.edu.hebtu.software.listendemo.credit.component.Day;
import cn.edu.hebtu.software.listendemo.credit.component.MonthPager;
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

import static android.content.Context.MODE_PRIVATE;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.SP_NAME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.USER_KEEP_KEY;

//自定义的Dialog需要继承DialogFragment
public class CustomDialogSign extends DialogFragment {
    private ThemeDayView themeDayView;
    private CalendarDate date;
    private HashMap<String, String> markData;
    private Activity context;
    private User user;
    private TextView dateTv;
    private CalendarRenderer renderer;
    private Handler handler;
    private static final int GET_SIGN_INFO = 800;
    private static final int GET_SIGN_RETROACTIVE_INFO = 1100;
    private SharedPreferences sp;
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private CalendarViewAdapter calendarAdapter;
    private TextView tvCreditSum;
    private TextView tvSignDayContinue;
    private TextView tvSignDaySum;
    private MonthPager monthPager;

    public CustomDialogSign() {
    }

    @SuppressLint("ValidFragment")
    public CustomDialogSign(User user, ThemeDayView themeDayView, CalendarDate date, HashMap<String, String> markData, Activity context, SharedPreferences sp, CalendarViewAdapter calendarAdapter, TextView tvCreditSum, TextView tvSignDayContinue, TextView tvSignDaySum, MonthPager monthPager) {
        this.themeDayView = themeDayView;
        this.date = date;
        this.markData = markData;
        this.context = context;
        this.calendarAdapter = calendarAdapter;
        this.sp = sp;
        this.tvCreditSum = tvCreditSum;
        this.tvSignDayContinue = tvSignDayContinue;
        this.tvSignDaySum = tvSignDaySum;
        this.monthPager = monthPager;
    }

    private void updateSp(User user0) {
        sp.edit().putString(USER_KEEP_KEY, gson.toJson(user0)).commit();
    }

    //重写方法onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //根据布局文件通过布局填充器创建view
        View view = inflater.inflate(R.layout.custom_dialog_sign, null);
        user = gson.fromJson(sp.getString(USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);

//        tvSignDayContinue = (TextView) context.findViewById(R.id.tv_sign_day_continue);
//        tvSignDaySum = (TextView) context.findViewById(R.id.tv_sign_day_sum);
//        tvCreditSum = (TextView) context.findViewById(R.id.tv_sign_point_sum);

        //获取布局文件的控件
        TextView tvContent = view.findViewById(R.id.tv_sign_content);
        TextView btnOK = view.findViewById(R.id.btn_ok_sign);
        TextView btnCancel = view.findViewById(R.id.btn_sign_back);
        CalendarDate currentDate = new CalendarDate();
        dateTv = (TextView) themeDayView.findViewById(R.id.date);
        int offset = Utils.calculateMonthOffset(date.getYear(), date.getMonth(), currentDate);
        if (offset == 0) {
            int offset1 = currentDate.getDay() - date.getDay();
            int daySum = Utils.getMonthDays(date.getYear(), date.getMonth());
            //今天
            if (date.getYear() == currentDate.getYear() && date.getMonth() == currentDate.getMonth() && date.getDay() == currentDate.getDay()) {
                String signDate = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                if (markData == null || markData.get(signDate).equals("1")) {
                    //用户签到
                    userSignIn(user.getUid());
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case GET_SIGN_INFO:
                                    if (msg.obj != null) {
                                        if (markData == null) {
                                            markData = new HashMap<>();
                                        }
                                        User user = new Gson().fromJson(msg.obj + "", User.class);
                                        tvCreditSum.setText(user.getUserCredit() + "分");
                                        tvSignDaySum.setText(user.getAccumulateSignIn() + "天");
                                        tvSignDayContinue.setText(user.getContinuousSignIn() + "天");
                                        Log.e("ttttttttttttt", user.toString());
                                        List<UnLock> unLocks = null;
                                        try {
                                            JSONObject jsonObject = new JSONObject(msg.obj+"");
                                            String unLockList = jsonObject.get("unlockList").toString();
                                            Type type = new TypeToken<List<UnLock>>(){}.getType();
                                            unLocks = new Gson().fromJson(unLockList,type);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        user.setUnLockList(unLocks);
                                        updateSp(user);
                                        context.getSharedPreferences(SP_NAME,MODE_PRIVATE).edit().putString(USER_KEEP_KEY,msg.obj+"").commit();
                                        markData.put(signDate, "0");
                                        tvContent.setText("签到成功！");
                                        dateTv.setText("签");
//                                        context.finish();
//                                        Intent intent = new Intent(context, SyllabusActivity.class);
//                                        startActivity(intent);
//                                        Log.e("ttttttttttttt", dateTv.getText() + "");
                                    } else {
                                        tvContent.setText("签到失败！");
                                    }
                                    break;
                            }
                        }
                    };
                } else {
                    dismissAllowingStateLoss();
                    getDialog().dismiss();
                    dateTv.setText("签");
                    tvContent.setText(" 今天已签到,不可重复签到哦！");
                }
                //补签
            } else if (currentDate.getDay() > date.getDay()) {
                dismissAllowingStateLoss();
                getDialog().dismiss();
                String signDate = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                if (null == markData || markData.get(signDate).equals("1")) {
                    AlertDialog.Builder adBuilder = new AlertDialog.Builder(context);
                    adBuilder.setTitle("补签");
                    if (user.getUserCredit() >= 2) {
                        adBuilder.setMessage("补签将消耗2积分，是否补签？");
                    } else {
                        adBuilder.setMessage("补签需消耗2积分，您的积分不足！");
                    }
                    adBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (user.getUserCredit() >= 2) {
                                retroactive(user.getUid(), signDate);
                                handler = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        switch (msg.what) {
                                            case GET_SIGN_RETROACTIVE_INFO:
                                                if (markData == null) {
                                                    markData = new HashMap<>();
                                                }
                                                if (msg.obj != null) {
                                                    Log.e("YYYYYYYYYYYY",msg.obj+"");
                                                    markData.put(signDate, "0");
                                                    Toast.makeText(context, "补签成功", Toast.LENGTH_SHORT).show();
//                                                    tvContent.setText("补签成功,消耗2积分");
//                                                     Log.e("ttttttttttttt",user.toString());
                                                    List<UnLock> unLocks = null;
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(msg.obj+"");
                                                        String unLockList = jsonObject.get("unlockList").toString();
                                                        Type type = new TypeToken<List<UnLock>>(){}.getType();
                                                        unLocks = new Gson().fromJson(unLockList,type);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    User user = new Gson().fromJson(msg.obj + "", User.class);
                                                    user.setUnLockList(unLocks);
                                                    updateSp(user);
                                                    context.getSharedPreferences(SP_NAME,MODE_PRIVATE).edit().putString(USER_KEEP_KEY,msg.obj+"").commit();
                                                    tvCreditSum.setText(user.getUserCredit() + "分");
                                                    tvSignDaySum.setText(user.getAccumulateSignIn() + "天");
                                                    tvSignDayContinue.setText(user.getContinuousSignIn() + "天");
//                                                    Intent intent = new Intent(context, SyllabusActivity.class);
//                                                    startActivity(intent);
//                                                    context.finish();
                                                } else {
                                                    //Toast.makeText(context, "积分不足，无法补签", Toast.LENGTH_SHORT).show();
                                                    //tvContent.setText("积分不足，补签失败！");
                                                }
                                                break;
                                        }
                                    }
                                };
                            } else {
                                tvContent.setText("积分不足，无法补签！");
                            }
                        }
                    });
                    adBuilder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    adBuilder.create().show();
//                    if (user.getUserCredit() > 2) {
////                        retroactive(user.getUid(), signDate);
////                        handler = new Handler() {
////                            @Override
////                            public void handleMessage(Message msg) {
////                                super.handleMessage(msg);
////                                switch (msg.what) {
////                                    case GET_SIGN_RETROACTIVE_INFO:
////                                        if (markData == null) {
////                                            markData = new HashMap<>();
////                                        }
////                                        if (msg.obj!=null) {
////                                            markData.put(signDate, "0");
////                                            tvContent.setText("补签成功,消耗2积分");
////
//////                                            context.finish();
//////                                            Intent intent = new Intent(context, SyllabusActivity.class);
//////                                            startActivity(intent);
//////                                            tvCreditSum.setText(user.getUserCredit() + "分");
//////                                            tvSignDaySum.setText(user.getAccumulateSignIn() + "天");
//////                                            tvSignDayContinue.setText(user.getContinuousSignIn() + "天");
//////                                            Log.e("ttttttttttttt",user.toString());
////                                            User user = new Gson().fromJson(msg.obj + "", User.class);
////                                            updateSp(user);
////                                        } else {
////                                            tvContent.setText("积分不足，补签失败！");
////                                        }
////                                        break;
////                                }
////                            }
////                        };
////                    } else {
////                        tvContent.setText("积分不足，无法补签！");
////                    }

                } else {
                    dismissAllowingStateLoss();
                    getDialog().dismiss();
                }
            } else {
                dismissAllowingStateLoss();
                getDialog().dismiss();
            }
        } else if (offset > 0) {
            dismissAllowingStateLoss();
            getDialog().dismiss();
        } else {
            tvContent.setText("只可补签当前月！");
        }


        //给按钮添加自定义的监听器
        CustomDialogListener listener = new CustomDialogListener();
        btnOK.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);

        //返回view
        return view;
    }

    private class CustomDialogListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_ok_sign:
                    refreshSelectBackground();
                    context.finish();
                    Intent intent = new Intent(context, SyllabusActivity.class);
                    startActivity(intent);
                    getDialog().cancel();
                    getDialog().dismiss();
                    break;
                case R.id.btn_sign_back:
                    refreshSelectBackground();
                    context.finish();
                    Intent intent1 = new Intent(context, SyllabusActivity.class);
                    startActivity(intent1);
                    getDialog().cancel();
                    getDialog().dismiss();
                    break;
            }
        }
    }

    public void userSignIn(int userId) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("id", userId + "").build();
        Request request = new Request.Builder().url(Constant.URL_SIGN_IN).post(fb).build();
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
                if (json != null || !json.equals("")) {
                    Message message = new Message();
                    message.what = GET_SIGN_INFO;
                    message.obj = json;
                    handler.sendMessage(message);
                }
            }

        });
    }

    public void retroactive(int userId, String date) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("id", userId + "").add("date", date).build();
        Request request = new Request.Builder().url(Constant.URL_SIGN_RETROACTIVE).post(fb).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("userReSignIn", "f");
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
                Log.e("userReSignIn", "" + json);
                if (json != null || !json.equals("")) {
                    Message message = new Message();
                    message.what = GET_SIGN_RETROACTIVE_INFO;
                    message.obj = json;
                    handler.sendMessage(message);
                }
            }

        });

    }

    public void refreshSelectBackground() {
        monthPager.invalidate();
        monthPager.postInvalidate();
        monthPager.requestLayout();
        context.invalidateOptionsMenu();
        calendarAdapter.setMarkData(markData);
        calendarAdapter.notifyDataChanged();
        calendarAdapter.notifyDataSetChanged();
        calendarAdapter.notifyDataChanged(new CalendarDate());


    }
}
