package cn.edu.hebtu.software.listendemo.Mine.index.credit;

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

import cn.edu.hebtu.software.listendemo.Mine.index.credit.SignActivity;
import cn.edu.hebtu.software.listendemo.R;

//自定义的Dialog需要继承DialogFragment
public class CustomDialogSignSuccess extends DialogFragment {
    private Activity context;
    private String signal;
    private int credit;
    private String reason;

    public CustomDialogSignSuccess() {
    }

    @SuppressLint("ValidFragment")
    public CustomDialogSignSuccess(Activity context, String signal,int credit,String reason) {
        this.signal=signal;
        this.context = context;
        this.credit = credit;
        this.reason=reason;
    }


    //重写方法onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //根据布局文件通过布局填充器创建view
        View view = inflater.inflate(R.layout.custom_dialog_sign_success, null);


        //获取布局文件的控件
        TextView tvSignSucessCredit = view.findViewById(R.id.tv_sign_sucess_credit);
        tvSignSucessCredit.setText("积分"+signal+credit);
        TextView tvSignSucessCreditRe=view.findViewById(R.id.tv_sign_sucess_credit_re);
        tvSignSucessCreditRe.setText(reason);
        Button btnSignSucessKnow = view.findViewById(R.id.btn_sign_sucess_know);
        btnSignSucessKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                getDialog().dismiss();
                context.finish();
                Intent intent1 = new Intent(context, SignActivity.class);
                startActivity(intent1);
            }
        });
        //返回view
        return view;
    }


}
