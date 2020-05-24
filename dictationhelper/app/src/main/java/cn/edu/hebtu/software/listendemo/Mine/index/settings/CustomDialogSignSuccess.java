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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.credit.Utils.Utils;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarDate;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarRenderer;
import cn.edu.hebtu.software.listendemo.credit.component.MonthPager;
import cn.edu.hebtu.software.listendemo.credit.view.CalendarViewAdapter;
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
