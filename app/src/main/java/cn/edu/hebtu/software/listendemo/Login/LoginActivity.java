package cn.edu.hebtu.software.listendemo.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import cn.edu.hebtu.software.listendemo.Host.index.ListenIndexActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final int LOGIN_MSG = 1;
    private EditText etPhone = null;
    private EditText etPassword = null;
    private ImageView ivShowPwd = null;
    private TextView tvWrongMsg = null;
    private ImageView ivLogin = null;
    private TextView tvForget = null;
    private TextView tvRegister = null;
    private TextView tvSLA = null;
    private OkHttpClient client = new OkHttpClient();
    private LoginActivityListener listener = new LoginActivityListener();
    private Gson gson = new Gson();
    private SharedPreferences sp = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_MSG:
                    tvWrongMsg.setText("");
                    setListeners();
                    if ((msg.obj.toString()).equals("")) {
                        // 登陆失败
                        tvWrongMsg.setText("登陆失败，请检查手机号或者密码是否正确!");
                    } else {
                        // 登陆成功，保存登陆信息自动登陆
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString(Constant.USER_KEEP_KEY , msg.obj.toString());
                        editor.putBoolean(Constant.AUTO_LOGIN_KEY , true);
                        editor.commit();
                        // 页面跳转
                        Intent intent = new Intent(LoginActivity.this, ListenIndexActivity.class);
                        startActivity(intent);
//                        finish();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();
        // 2. 判断sharedP中是否已经有登录用户
        sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        if (sp.getBoolean(Constant.AUTO_LOGIN_KEY, Constant.DEFAULT_LOGIN_KEY)) {
            // 此时不为第一次登陆或退出登录情况，自动登陆
            Intent intent = new Intent(this, ListenIndexActivity.class);
            startActivity(intent);
        } else {
            setListeners();
        }
    }

    private void setListeners(){
        ivLogin.setOnClickListener(listener);
        ivShowPwd.setOnClickListener(listener);
        tvRegister.setOnClickListener(listener);
        tvForget.setOnClickListener(listener);
        tvSLA.setOnClickListener(listener);
    }

    private class LoginActivityListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_login_show_pwd:
                    // 当当前 password 文本为 "明文" 形式
                    if (etPassword.getInputType() == 128) {
                        // 1. 将password 的输入框文本变为 "密码" 形式
                        etPassword.setInputType(129);
                        // 2. 将图片变为close
                        ivShowPwd.setImageResource(R.drawable.eye_close);
                    } else if (etPassword.getInputType() == 129) {   // 当前为 "密码" 格式
                        // 1. 变为文本格式
                        etPassword.setInputType(128);
                        // 2. 修改图片
                        ivShowPwd.setImageResource(R.drawable.eye_open);
                    }
                    break;
                case R.id.iv_login:
                    // 1. 判断是否填入登陆信息
                    tvWrongMsg.setText("");
                    if (etPhone.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                        tvWrongMsg.setText("请填入登陆信息！");
                    } else {// 2. 获取登陆内容
                        FormBody fb = new FormBody.Builder()
                                .add("", etPhone.getText().toString())
                                .add("", etPassword.getText().toString())
                                .build();
                        Request request = new Request.Builder()
                                .url(Constant.URL_USER_LOGIN)
                                .post(fb)
                                .build();
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String jsonUser = response.body().string();
                                Message message = new Message();
                                message.what = LOGIN_MSG;
                                message.obj = jsonUser;
                                handler.sendMessage(message);
                            }
                        });
                    }
                    break;
                case R.id.tv_login_regist_user:
                    // 跳转至注册界面
                    Intent intent = new Intent(LoginActivity.this,RegistActivity.class);
                    startActivity(intent);
                    break;
                case R.id.tv_login_forget_pwd:
                    break;
                case R.id.tv_login_read_sla:
                    break;
            }
        }
    }

    private void findViews() {
        etPhone = findViewById(R.id.et_login_phone);
        etPassword = findViewById(R.id.et_login_password);
        ivShowPwd = findViewById(R.id.iv_login_show_pwd);
        tvWrongMsg = findViewById(R.id.tv_login_wrongMsg);
        ivLogin = findViewById(R.id.iv_login);
        tvForget = findViewById(R.id.tv_login_forget_pwd);
        tvRegister = findViewById(R.id.tv_login_regist_user);
        tvSLA = findViewById(R.id.tv_login_read_sla);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListeners();
    }
}
