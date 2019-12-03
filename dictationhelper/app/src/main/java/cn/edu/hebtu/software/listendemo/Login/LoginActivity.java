package cn.edu.hebtu.software.listendemo.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.hebtu.software.listendemo.Host.index.ListenIndexActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.edu.hebtu.software.listendemo.Untils.Constant.LOGIN_BY_PASSWORD;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.LOGIN_BY_VERIFY;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.LOGIN_PASSWORD_SUCCESS;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.LOGIN_PASSWORD_UNREGIST;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.LOGIN_PASSWORD_WRONGPASS;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.LOGIN_PHONE_REGISTED;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.LOGIN_PHONE_UNREGIST;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.URL_LOGIN_VERIFY;

public class LoginActivity extends AppCompatActivity {
    private static final int LOGIN_MSG = 1;
    private static final int VERIFY_MSG = 2;

    private int WAY_OF_LOGIN = LOGIN_BY_VERIFY; //默认登录方式为验证码 登录

    private EditText etPhone = null;
    private EditText etPassword = null;
    private ImageView ivShowPwd = null;
    private TextView tvWrongMsg = null;
    private ImageView ivLogin = null;
    private TextView tvForget = null;
    private TextView tvRegister = null;
    private TextView tvSLA = null;
    private TextView tvVerify = null;
    private TextView tv_login_forget = null;
    private TextView tv_login_password = null;


    private OkHttpClient client = new OkHttpClient();
    private LoginActivityListener listener = new LoginActivityListener();
    private Gson gson = new Gson();
    private SharedPreferences sp = null;
    public EventHandler eh; //事件接收器


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_MSG:
                    //处理密码登录获取的结果

                    tvWrongMsg.setText("");
                    setListeners();
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        switch (jsonObject.getInt("register_type")){
                            case LOGIN_PASSWORD_SUCCESS:
                                //登陆成功
                                //保存登录信息
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(Constant.USER_KEEP_KEY , jsonObject.getString("user"));
                                editor.putBoolean(Constant.AUTO_LOGIN_KEY , true);
                                editor.commit();
                                // 页面跳转
                                Intent intent = new Intent(LoginActivity.this, ListenIndexActivity.class);
                                startActivity(intent);
                                break;

                            case LOGIN_PASSWORD_UNREGIST:
                                tvWrongMsg.setText("该手机号还未注册，请使用验证码登录来完成注册");
                                //修改ui
                                etPassword.setText("");
                                etPassword.setInputType(128);
                                tv_login_password.setText("密码登录");
                                tvVerify.setVisibility(View.VISIBLE);
                                ivShowPwd.setVisibility(View.INVISIBLE);
                                etPassword.setHint("输入验证码");
                                WAY_OF_LOGIN = LOGIN_BY_VERIFY;
                                break;

                            case LOGIN_PASSWORD_WRONGPASS:
                                tvWrongMsg.setText("密码错误，请重新输入");
                                etPassword.setText("");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;

                case VERIFY_MSG:
                    //处理验证码登录的服务器返回值
                    try {

                        JSONObject jsonObject = new JSONObject(msg.obj.toString());

                        switch (jsonObject.getInt("register_type")){
                            case LOGIN_PHONE_UNREGIST:
                                //第一次注册登录
                                //do something
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(Constant.USER_KEEP_KEY , jsonObject.getString("user"));
                                editor.putBoolean(Constant.AUTO_LOGIN_KEY , true);
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this, CompleteInformationActivity.class);
                                startActivity(intent);
                                finish();
                                break;

                            case LOGIN_PHONE_REGISTED:
                                //登陆成功,保存用户信息到本地

                                //登陆成功
                                //保存登录信息
                                editor = sp.edit();
                                editor.putString(Constant.USER_KEEP_KEY , jsonObject.getString("user"));
                                editor.putBoolean(Constant.AUTO_LOGIN_KEY , true);
                                editor.commit();
                                // 页面跳转
                                intent = new Intent(LoginActivity.this, ListenIndexActivity.class);
                                startActivity(intent);
                                finish();

                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化即创建语音配置对象
//        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=5de5adc2");

        init();//初始化事件接收器

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
        tvVerify.setOnClickListener(listener);
        tv_login_password.setOnClickListener(listener);
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
                    if(WAY_OF_LOGIN == LOGIN_BY_VERIFY){
                        tvWrongMsg.setText("");
                        if(etPhone.getText().toString().equals("") ||etPassword.getText().toString().equals("")){
                            tvWrongMsg.setText("请填入登陆信息！");
                        }else{
                            if(checkTel(etPhone.getText().toString())){
                                SMSSDK.submitVerificationCode("+86",etPhone.getText().toString(),etPassword.getText().toString());
                            }else{
                                tvWrongMsg.setText("请填入正确的手机号！");
                            }

                        }


                    }else{
                        //手机号密码登录
                        // 1. 判断是否填入登陆信息
                        tvWrongMsg.setText("");
                        if (etPhone.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                            tvWrongMsg.setText("请填入登陆信息！");
                        } else {// 2. 获取登陆内容
                            FormBody fb = new FormBody.Builder()
                                    .add("login_type","2")
                                    .add("phone", etPhone.getText().toString())
                                    .add("password", etPassword.getText().toString())
                                    .build();
                            Request request = new Request.Builder()
                                    .url(Constant.URL_LOGIN_VERIFY)
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
                                    String receiveJson = response.body().string();
                                    Message message = new Message();
                                    message.what = LOGIN_MSG;
                                    message.obj = receiveJson;
                                    handler.sendMessage(message);
                                }
                            });
                        }
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
                case R.id.tv_verify:
                    //获取验证码的点击事件
                    hideInputMethod(getApplicationContext(),getCurrentFocus());
                    if(!etPhone.getText().toString().trim().equals("") && checkTel(etPhone.getText().toString().trim())){
                        SMSSDK.getVerificationCode("+86", etPhone.getText().toString());//获取验证码
                    }else{
                        tvWrongMsg.setText("请填入正确的手机号！");
                    }
                    break;
                case R.id.tv_login_forget:
                    //忘记密码的点击


                    break;

                case R.id.tv_login_password:
                    /**
                     * 判断是否WAY_OF_LOGIN为LOGIN_BY_VERIFY
                     *
                     * 清空etPassword的内容
                     * 设置etPassword输入内容为password
                     * 改变tv_login_password的内容为快速登录
                     * 改变tvVerify为invisible
                     * 改变ivShowPwd为visible
                     * 改变etPassword的hint为输入密码
                     * 改变WAY_OF_LOGIN为LOGIN_BY_PASSWORD
                     *
                     * 如果WAY_OF_LOGIN为LOGIN_BY_PASSWORD
                     * 相反的操作
                     *
                     *
                     */
                    if(WAY_OF_LOGIN == LOGIN_BY_VERIFY){
                        etPassword.setText("");
                        etPassword.setInputType(129);
                        tv_login_password.setText("快速登录");
                        tvVerify.setVisibility(View.INVISIBLE);
                        ivShowPwd.setVisibility(View.VISIBLE);
                        etPassword.setHint("输入密码");
                        WAY_OF_LOGIN = LOGIN_BY_PASSWORD;
                    }else{
                        etPassword.setText("");
                        etPassword.setInputType(128);
                        tv_login_password.setText("密码登录");
                        tvVerify.setVisibility(View.VISIBLE);
                        ivShowPwd.setVisibility(View.INVISIBLE);
                        etPassword.setHint("输入验证码");
                        WAY_OF_LOGIN = LOGIN_BY_VERIFY;
                    }
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
        tvVerify = findViewById(R.id.tv_verify);
        tv_login_forget = findViewById(R.id.tv_login_forget);
        tv_login_password = findViewById(R.id.tv_login_password);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListeners();
    }
    public boolean checkTel(String tel) {
        Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
        Matcher matcher = p.matcher(tel);
        return matcher.matches();
    }


    /**
     * 初始化事件接收器
     */
    private void init() {
        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码成功
                        tvWrongMsg.setText("验证成功，等待服务器返回数据...");



                        //发送请求
//                        JSONObject jsonObject = new JSONObject();
//                        try {
//                            jsonObject.put("login_type",1);
//                            jsonObject.put("phone",etPhone.getText().toString());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(1000*60, TimeUnit.MILLISECONDS).build();
//                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonObject.toString());
                        FormBody formBody = new FormBody.Builder()
                                .add("login_type","1")
                                .add("phone",etPhone.getText().toString())
                                .build();

                        final Request request = new Request.Builder().url(URL_LOGIN_VERIFY).post(formBody).build();
                            Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {

                                String receiveJson = response.body().string();
                                Message message = new Message();
                                message.what = VERIFY_MSG;
                                message.obj = receiveJson;
                                handler.sendMessage(message);

                            }
                        });

                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //发送之后
                                tvWrongMsg.setText("验证码已发送！");
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) { //返回支持发送验证码的国家列表

                    }else{
                        Log.e("验证码验证失败","错误的验证码");
                        tvWrongMsg.setText("验证码错误！");
                    }

                } else {
                    int status = 0;
                    try {
                        ((Throwable) data).printStackTrace();
                        Throwable throwable = (Throwable) data;
                        JSONObject object = new JSONObject(throwable.getMessage());
                        String des = object.optString("detail");
                        status = object.optInt("status");
                        if (!TextUtils.isEmpty(des)) {
                            return;
                        }
                    } catch (Exception e) {
                        SMSLog.getInstance().w(e);
                    }
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }


}
