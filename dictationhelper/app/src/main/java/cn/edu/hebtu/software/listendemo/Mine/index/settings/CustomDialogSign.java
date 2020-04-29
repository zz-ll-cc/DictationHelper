package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.credit.Utils.Utils;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarDate;
import cn.edu.hebtu.software.listendemo.credit.view.Calendar;
import cn.edu.hebtu.software.listendemo.credit.view.ThemeDayView;

//自定义的Dialog需要继承DialogFragment
public class CustomDialogSign extends DialogFragment {
    private ThemeDayView themeDayView;
    private CalendarDate date;
    private HashMap<String, String> markData;
    private Activity context;

    public CustomDialogSign() {
    }

    @SuppressLint("ValidFragment")
    public CustomDialogSign(ThemeDayView themeDayView, CalendarDate date, HashMap<String, String> markData, Activity context) {
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
        int offset = Utils.calculateMonthOffset(date.getYear(), date.getMonth(), currentDate);
        if (offset == 0) {
            int offset1 = currentDate.getDay() - date.getDay();
            int daySum = Utils.getMonthDays(date.getYear(), date.getMonth());
            //今天
            if (date.getYear() == currentDate.getYear() && date.getMonth() == currentDate.getMonth() && date.getDay() == currentDate.getDay()) {
                String signDate = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                if (markData.get(signDate) == null || markData.get(signDate).equals("")) {
                    markData.put(signDate, "0");
                    Log.e("click", "   签到成功!   ");
                    tvContent.setText("签到成功！");
                    TextView dateTv = (TextView) themeDayView.findViewById(R.id.date);
                    dateTv.setText("签");
                    Log.e("click", dateTv.getText() + "");
                } else {
                    TextView dateTv = (TextView) themeDayView.findViewById(R.id.date);
                    dateTv.setText("签");
                    Log.e("click", dateTv.getText() + "");
                    Log.e("click", "今天已签到,不可重复签到哦！");
                    tvContent.setText("今天已签到，不可重复签到哦！");
                }
                //补签
            } else if (currentDate.getDay() > date.getDay()) {
                String signDate = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
                if (markData.get(signDate) == null || markData.get(signDate).equals("")) {
                    markData.put(signDate, "0");
                    Log.e("click", "   补签成功!   ");
                    tvContent.setText("补签成功！");
                } else {
                    dismissAllowingStateLoss();
                    getDialog().dismiss();
                }
            } else {
                dismissAllowingStateLoss();
                getDialog().dismiss();
            }
        } else if (offset > 0) {
            Log.e("click", "未来！");
            dismissAllowingStateLoss();
            getDialog().dismiss();
        } else {
            Log.e("click", "只可补签当前月！");
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
                    getDialog().dismiss();
                    break;
                case R.id.btn_sign_back:
                    getDialog().dismiss();
                    break;
            }
        }
    }
}
