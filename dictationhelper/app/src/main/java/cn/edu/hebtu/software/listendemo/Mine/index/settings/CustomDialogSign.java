package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;

import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.credit.Utils.Utils;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarDate;
import cn.edu.hebtu.software.listendemo.credit.view.ThemeDayView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//自定义的Dialog需要继承DialogFragment
public class CustomDialogSign extends DialogFragment {
    private ThemeDayView themeDayView;
    private CalendarDate date;
    private HashMap<String, String> markData;
    private Activity context;
    private User user;
    private TextView dateTv;
    private Handler handler;
    private static final int GET_SIGN_INFO = 800;
    private static final int GET_SIGN_RETROACTIVE_INFO=1100;

    public CustomDialogSign() {
    }

    @SuppressLint("ValidFragment")
    public CustomDialogSign(User user, ThemeDayView themeDayView, CalendarDate date, HashMap<String, String> markData, Activity context) {
        this.user = user;
        this.themeDayView = themeDayView;
        this.date = date;
        this.markData = markData;
        this.context = context;
    }

    //重写方法onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //根据布局文件通过布局填充器创建view
        View view = inflater.inflate(R.layout.custom_dialog_sign, null);

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
                if (markData.get(signDate) == null || markData.get(signDate).equals("")) {
                    //用户签到
                    userSignIn(user.getUid());
                    Log.e("userSignIn","用户签到");
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case GET_SIGN_INFO:
                                    if (msg.obj.equals("1")) {
                                        markData.put(signDate, "0");
                                        tvContent.setText("签到成功！");
                                        dateTv.setText("签");
                                    } else {
                                        tvContent.setText("签到失败！");
                                    }
                                    break;
                            }
                        }
                    };
                } else {
                    dateTv.setText("签");
                    tvContent.setText("今天已签到，不可重复签到哦！");
                }
                //补签
            } else if (currentDate.getDay() > date.getDay()) {
                String signDate = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                if (markData.get(signDate) == null || markData.get(signDate).equals("1")) {
                    Log.e("userReSignIn","补签中");
                    retroactive(user.getUid(),signDate);
                    handler = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case GET_SIGN_RETROACTIVE_INFO:
                                    markData.put(signDate, "0");
                                    if (msg.obj.equals("1")) {
                                        tvContent.setText("补签成功！");
                                    } else {
                                        tvContent.setText("补签失败！");
                                    }
                                    break;
                            }
                        }
                    };
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
                    context.finish();
                    Intent intent = new Intent(context, SyllabusActivity.class);
                    startActivity(intent);
                    getDialog().dismiss();
                    break;
                case R.id.btn_sign_back:
                    context.finish();
                    Intent intent1 = new Intent(context, SyllabusActivity.class);
                    startActivity(intent1);
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
                Log.e("userSignIn", "" + json);
                if (json != null || !json.equals("")) {
                    Message message = new Message();
                    message.what = GET_SIGN_INFO;
                    message.obj ="1";
                    handler.sendMessage(message);
                }
            }

        });
    }

    public void retroactive(int userId,String date){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("id", userId + "").add("date",date).build();
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
                    message.obj = "1";
                    handler.sendMessage(message);
                }
            }

        });

    }
}
