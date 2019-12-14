//package cn.edu.hebtu.software.listendemo.Login;
//
//
//import android.Manifest;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import cn.edu.hebtu.software.listendemo.R;
//import cn.smssdk.SMSSDK;
//
//
//public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//    public static final String TAG = "LoginActivity";
//    String APPKEY = "2cd22ca0e4a00";
//    String APPSECRET = "a5ffadf025c1168760169d0fd556b054";
//    private ImageView imageView;
//    private EditText et_phoneNum, et_password;
//    private Button btn_login, btn_getMsg;
//    private int i = 30;//计时器
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        //如果 targetSdkVersion小于或等于22，可以忽略这一步，如果大于或等于23，需要做权限的动态申请：
//        if (Build.VERSION.SDK_INT >= 23) {
//            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
//            ActivityCompat.requestPermissions(this, mPermissionList, 123);
//        }
//        //onCreate里注册
//        initView();
//       /* // 启动短信验证sdk
//        MobSDK.init(this, APPKEY, APPSECRET);
//        EventHandler eventHandler = new EventHandler() {
//            @Override
//            public void afterEvent(int i, int i1, Object o) {
//                Message message = new Message();
//                message.arg1 = i;
//                message.arg2 = i1;
//                message.obj = o;
//                handler.sendMessage(message);
//            }
//            //注册回调监听接口
//        };
//        SMSSDK.registerEventHandler(eventHandler);*/
//
//    }
//
//    private void initView() {
//        et_phoneNum = findViewById(R.id.mobile_login);
//        et_password = findViewById(R.id.yanzhengma);
//        //btn_getMsg = findViewById(R.id.getyanzhengma1);
//        btn_login = findViewById(R.id.login_btn);
//        btn_login.setOnClickListener(this);
//        btn_getMsg.setOnClickListener(this);
//    }
//
//
//   /* Handler handler = new Handler() {
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            if (msg.what == -9) {
//                btn_getMsg.setText("重新发送(" + i + ")");
//            } else if (msg.what == -8) {
//                btn_getMsg.setText("获取验证码");
//                btn_getMsg.setClickable(true);
//                i = 30;
//            } else {
//                int i = msg.arg1;
//                int i1 = msg.arg2;
//                Object o = msg.obj;
//                if (i1 == SMSSDK.RESULT_COMPLETE) {
//                    // 短信注册成功后，返回MainActivity,然后提示
//                    if (i == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
//                        Toast.makeText(LoginActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("userName", et_phoneNum.getText().toString().trim());
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//
//                    } else if (i == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
//                        Toast.makeText(LoginActivity.this, "正在获取验证码", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }
//    };*/
//
//
//    @Override
//    public void onClick(View view) {
//        String phoneNum = et_phoneNum.getText().toString();
//        switch (view.getId()) {
//            //case R.id.getyanzhengma1:
//              /*  // 1. 判断手机号是不是11位并且看格式是否合理
//                if (!judgePhoneNums(phoneNum)) {
//                    return;
//                }
//                // 2. 通过sdk发送短信验证
//                SMSSDK.getVerificationCode("86", phoneNum);
//                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
//                btn_getMsg.setClickable(false);
//                btn_getMsg.setText("重新发送(" + i + ")");
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (; i > 0; i--) {
//                            handler.sendEmptyMessage(-9);
//                            if (i <= 0) {
//                                break;
//                            }
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        handler.sendEmptyMessage(-8);
//                    }
//                }).start();
//                break;*/
//
//            case R.id.login_btn:
//              /*  if (et_phoneNum == null) {
//                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "onClick: 手机号不能为空");
//                }
//                if (et_password == null) {
//                    Toast.makeText(LoginActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
//                }
//                //将收到的验证码和手机号提交再次核对
//                SMSSDK.submitVerificationCode("86", phoneNum, et_password
//                        .getText().toString());*/
//                break;
//        }
//    }
//
//    /**
//     * 判断手机号码是否合理
//     *
//     * @param phoneNums
//     */
//    private boolean judgePhoneNums(String phoneNums) {
//        if (isMatchLength(phoneNums, 11)
//                && isMobileNO(phoneNums)) {
//            return true;
//        }
//        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
//        return false;
//    }
//
//    /**
//     * 判断一个字符串的位数
//     *
//     * @param str
//     * @param length
//     * @return
//     */
//    public static boolean isMatchLength(String str, int length) {
//        if (str.isEmpty()) {
//            return false;
//        } else {
//            return str.length() == length ? true : false;
//        }
//    }
//
//    /**
//     * 验证手机格式
//     */
//    public static boolean isMobileNO(String mobileNums) {
//        /*
//         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
//         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
//         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
//         */
//        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//        if (TextUtils.isEmpty(mobileNums))
//            return false;
//        else
//            return mobileNums.matches(telRegex);
//    }
//
//    @Override
//    protected void onDestroy() {
//        //反注册回调监听接口
//        SMSSDK.unregisterAllEventHandler();
//        super.onDestroy();
//    }
//
//}
//
