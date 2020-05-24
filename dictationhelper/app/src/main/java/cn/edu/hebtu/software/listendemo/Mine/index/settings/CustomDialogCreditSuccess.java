package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.edu.hebtu.software.listendemo.R;

//自定义的Dialog需要继承DialogFragment
public class CustomDialogCreditSuccess extends DialogFragment {
    private Activity context;
    private String signal;
    private int credit;

    public CustomDialogCreditSuccess() {
    }

    @SuppressLint("ValidFragment")
    public CustomDialogCreditSuccess(Activity context, String signal, int credit) {
        this.signal = signal;
        this.context = context;
        this.credit = credit;
    }


    //重写方法onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //根据布局文件通过布局填充器创建view
        View view = inflater.inflate(R.layout.custom_dialog_credit_success, null);


        //获取布局文件的控件
        TextView tvCreditFinish = view.findViewById(R.id.tv_credit_finish);
        TextView tvCreditSucessKnow = view.findViewById(R.id.tv_credit_sucess_know);
        tvCreditFinish.setText("积分"+signal+credit);
        tvCreditSucessKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                getDialog().dismiss();
            }
        });
        //返回view
        return view;
    }


}
