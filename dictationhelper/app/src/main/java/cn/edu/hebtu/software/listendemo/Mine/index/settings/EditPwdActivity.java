package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Base64;

import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditPwdActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    // TODO: 2019/12/5 验证码
    private int type;   // 当前状态位，设置密码/通过旧密码修改密码/通过手机验证修改密码
    private User user;
    private SharedPreferences sp;
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private Gson gson = new Gson();
    private ImageView ivExit;
    private TextView tvTitle;
    private LinearLayout llSetPwd;
    private LinearLayout llUpdPwd;
    private LinearLayout llUpdByPhone;
    private LinearLayout llUpdByOld;
    private EditText etSetPwd;
    private ImageView ivSetClean;
    private EditText etOldPwd;
    private ImageView ivOldClean;
    private EditText etNewPwdO;
    private ImageView ivNewCleanO;
    private Button btnSave;
    private TextView tvPhoneNumber;
    private EditText etCode;
    private Button btnGetCode;
    private EditText etNewPwdP;
    private ImageView ivNewCleanP;
    private TextView tvSetError;
    private TextView tvOldError;
    private TextView tvNewErrorO;
    private TextView tvCodeError;
    private TextView tvNewErrorP;
    private TextView tvChange;
    private String phoneNumber;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_PWD:
                    User user = gson.fromJson(msg.obj.toString(), User.class);
                    sp.edit().putString(Constant.USER_KEEP_KEY, gson.toJson(user)).commit();
                    Toast.makeText(EditPwdActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case UPD_OLD:
                    try {
                        JSONObject obj = new JSONObject(msg.obj.toString());
                        if (obj.getBoolean("type")) {
                            // 更改成功
                            User user2 = gson.fromJson(obj.getString("user"), User.class);
                            sp.edit().putString(Constant.USER_KEEP_KEY, gson.toJson(user2)).commit();
                            Toast.makeText(EditPwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            tvOldError.setText("密码输入错误");
                            etOldPwd.setText("");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case UPD_PHONE:
                    User user1 = gson.fromJson(msg.obj.toString(), User.class);
                    sp.edit().putString(Constant.USER_KEEP_KEY, gson.toJson(user1)).commit();
                    Toast.makeText(EditPwdActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
        }
    };
    private static final int SET_PWD = 100;
    private static final int UPD_OLD = 200;
    private static final int UPD_PHONE = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd);

        findViews();
        initData();
        initView();
        setOnFocusListener();
        setOnClickListeners();
    }

    private void setOnFocusListener() {
        etCode.setOnFocusChangeListener(this);
        etNewPwdP.setOnFocusChangeListener(this);
        etSetPwd.setOnFocusChangeListener(this);
        etOldPwd.setOnFocusChangeListener(this);
        etNewPwdO.setOnFocusChangeListener(this);
    }

    private void setOnClickListeners() {
        ivExit.setOnClickListener(this);
        tvChange.setOnClickListener(this);
        ivNewCleanO.setOnClickListener(this);
        ivNewCleanP.setOnClickListener(this);
        ivOldClean.setOnClickListener(this);
        ivSetClean.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnGetCode.setOnClickListener(this);
    }

    private void initView() {
        tvSetError.setVisibility(View.INVISIBLE);
        tvCodeError.setVisibility(View.INVISIBLE);
        tvOldError.setVisibility(View.INVISIBLE);
        tvNewErrorO.setVisibility(View.INVISIBLE);
        tvNewErrorP.setVisibility(View.INVISIBLE);
        tvPhoneNumber.setText(phoneNumber);
        if (null == user.getUpassword() || user.getUpassword().equals("") || user.getUpassword().equals("null")) {
            llSetPwd.setVisibility(View.VISIBLE);
            llUpdPwd.setVisibility(View.GONE);
            tvTitle.setText("设置密码");
            type = Constant.PWD_TYPE_SET;
        } else {
            llUpdPwd.setVisibility(View.VISIBLE);
            llSetPwd.setVisibility(View.GONE);
            tvTitle.setText("修改密码");
            // 默认当前界面是使用 旧密码
            llUpdByOld.setVisibility(View.VISIBLE);
            llUpdByPhone.setVisibility(View.GONE);
            type = Constant.PWD_TYPE_UPD_OLD;
        }
    }

    private void changeShowType() {
        if (type == Constant.PWD_TYPE_UPD_OLD) {
            // 状态位为 使用旧密码时
            type = Constant.PWD_TYPE_UPD_PHONE;
            llUpdByPhone.setVisibility(View.VISIBLE);
            llUpdByOld.setVisibility(View.GONE);
            tvOldError.setVisibility(View.INVISIBLE);
            tvNewErrorO.setVisibility(View.INVISIBLE);
        } else {
            // 状态位为 使用手机验证登陆时
            type = Constant.PWD_TYPE_UPD_OLD;
            llUpdByOld.setVisibility(View.VISIBLE);
            llUpdByPhone.setVisibility(View.GONE);
            tvCodeError.setVisibility(View.INVISIBLE);
            tvNewErrorP.setVisibility(View.INVISIBLE);
        }
    }

    private void initData() {
        sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        user = gson.fromJson(sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        String uphone = user.getUphone();
        phoneNumber = uphone.substring(0, 3) + "XXXX" + uphone.substring(7, uphone.length());
    }

    private void findViews() {
        ivExit = findViewById(R.id.iv_edit_my_msg_exit);
        tvTitle = findViewById(R.id.tv_upd_my_pwd_title);
        llSetPwd = findViewById(R.id.ll_dont_have_my_pwd);
        etSetPwd = findViewById(R.id.et_upd_my_set_password);
        ivSetClean = findViewById(R.id.iv_clean_upd_my_set_password);
        tvSetError = findViewById(R.id.tv_upd_my_set_pwd_error);

        llUpdPwd = findViewById(R.id.ll_have_my_pwd);
        llUpdByOld = findViewById(R.id.ll_have_my_pwd_by_old);
        etOldPwd = findViewById(R.id.et_upd_my_old_password);
        ivOldClean = findViewById(R.id.iv_clean_upd_my_old_password);
        tvOldError = findViewById(R.id.tv_old_pwd_error);
        etNewPwdO = findViewById(R.id.et_upd_my_password);
        ivNewCleanO = findViewById(R.id.iv_clean_upd_my_password);
        tvNewErrorO = findViewById(R.id.tv_upd_my_new_pwd_error);

        llUpdByPhone = findViewById(R.id.ll_have_my_pwd_by_phone);
        tvPhoneNumber = findViewById(R.id.tv_upd_my_pwd_phone);
        etCode = findViewById(R.id.et_upd_my_input_code);
        btnGetCode = findViewById(R.id.tv_upd_my_pwd_get_code);
        tvCodeError = findViewById(R.id.tv_upd_my_pwd_code_error);
        etNewPwdP = findViewById(R.id.et_upd_my_password_phone);
        ivNewCleanP = findViewById(R.id.iv_clean_upd_my_password_phone);
        tvNewErrorP = findViewById(R.id.tv_upd_my_new_pwd_error_phone);

        btnSave = findViewById(R.id.btn_upd_my_save_password);
        tvChange = findViewById(R.id.tv_upd_my_pwd_change_type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_upd_my_pwd_get_code:
                // TODO: 2019/12/5 获取验证码

                break;
            case R.id.btn_upd_my_save_password:
                switch (type) {
                    case Constant.PWD_TYPE_SET:
                        if (checkInputRight()) {
                            String pwd = "";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Base64.Encoder encoder = Base64.getEncoder();
                                pwd = new String (encoder.encode(etSetPwd.getText().toString().getBytes()));
                            }else{
                                pwd = etSetPwd.getText().toString();
                            }
                            FormBody fb = new FormBody.Builder()
                                    .add("type", Constant.PWD_TYPE_SET + "")
                                    .add("uid", user.getUid() + "")
                                    .add("upassword", pwd).build();
                            Request request = new Request.Builder()
                                    .url(Constant.URL_UPD_PWD)
                                    .post(fb).build();
                            Call call = client.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Message message = new Message();
                                    message.obj = response.body().string();
                                    message.what = SET_PWD;
                                    handler.sendMessage(message);
                                }
                            });
                        }
                        break;
                    case Constant.PWD_TYPE_UPD_OLD:
                        if (checkInputRight()) {
                            String pwdOld = "";
                            String pwdNew = "";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Base64.Encoder encoder = Base64.getEncoder();
                                pwdOld = new String (encoder.encode(etOldPwd.getText().toString().getBytes()));
                                pwdNew = new String (encoder.encode(etNewPwdO.getText().toString().getBytes()));
                            }else{
                                pwdOld = etOldPwd.getText().toString();
                                pwdNew = etNewPwdO.getText().toString();
                            }
                            FormBody fb = new FormBody.Builder().add("type", Constant.PWD_TYPE_UPD_OLD + "")
                                    .add("uid", user.getUid() + "")
                                    .add("upasswordOld", pwdOld)
                                    .add("upasswordNew", pwdNew).build();
                            Request request = new Request.Builder()
                                    .url(Constant.URL_UPD_PWD)
                                    .post(fb).build();
                            Call call = client.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Message message = new Message();
                                    message.what = UPD_OLD;
                                    message.obj = response.body().string();
                                    handler.sendMessage(message);
                                }
                            });
                        }
                        break;
                    case Constant.PWD_TYPE_UPD_PHONE:
                        if (checkInputRight()) {
                            String pwd = "";
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Base64.Encoder encoder = Base64.getEncoder();
                                pwd = new String (encoder.encode(etNewPwdP.getText().toString().getBytes()));
                            }else{
                                pwd = etNewPwdP.getText().toString();
                            }
                            FormBody fb = new FormBody.Builder().add("type", Constant.PWD_TYPE_UPD_PHONE + "")
                                    .add("uid", user.getUid() + "")
                                    .add("upassword", pwd).build();
                            Request request = new Request.Builder()
                                    .url(Constant.URL_UPD_PWD)
                                    .post(fb).build();
                            Call call = client.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Message message = new Message();
                                    message.what = UPD_PHONE;
                                    message.obj = response.body().string();
                                    handler.sendMessage(message);
                                }
                            });
                        }
                        break;
                }
                break;
            case R.id.iv_clean_upd_my_set_password:
                etSetPwd.setText("");
                break;
            case R.id.iv_clean_upd_my_old_password:
                etOldPwd.setText("");
                break;
            case R.id.iv_clean_upd_my_password:
                etNewPwdO.setText("");
                break;
            case R.id.iv_clean_upd_my_password_phone:
                etNewPwdP.setText("");
                break;
            case R.id.tv_upd_my_pwd_change_type:
                changeShowType();
                break;
            case R.id.iv_edit_my_msg_exit:
                finish();
                break;
        }
    }

    private boolean checkInputRight() {
        switch (type) {
            case Constant.PWD_TYPE_SET:
                String setPwd = etSetPwd.getText().toString();
                if (checkPwdRight(setPwd))
                    return true;
                else {
                    tvSetError.setVisibility(View.VISIBLE);
                    return false;
                }
            case Constant.PWD_TYPE_UPD_OLD:
                String oldPwd = etOldPwd.getText().toString();
                String newPwd = etNewPwdO.getText().toString();
                if (checkPwdRight(oldPwd) && checkPwdRight(newPwd)) {
                    return true;
                } else if (checkPwdRight(oldPwd) && !checkPwdRight(newPwd)) {
                    tvOldError.setVisibility(View.GONE);
                    tvNewErrorO.setVisibility(View.VISIBLE);
                    return false;
                } else if (!checkPwdRight(oldPwd) && checkPwdRight(newPwd)) {
                    tvOldError.setVisibility(View.VISIBLE);
                    tvNewErrorO.setVisibility(View.GONE);
                    return false;
                } else {
                    tvNewErrorO.setVisibility(View.VISIBLE);
                    tvOldError.setVisibility(View.VISIBLE);
                    return false;
                }
            case Constant.PWD_TYPE_UPD_PHONE:
                String pwd = etNewPwdP.getText().toString();
                if (checkCodeRight() && checkPwdRight(pwd))
                    return true;
                else if (checkCodeRight() && !checkPwdRight(pwd)) {
                    tvCodeError.setVisibility(View.GONE);
                    tvNewErrorP.setVisibility(View.VISIBLE);
                    return false;
                } else if (checkCodeRight() && checkPwdRight(pwd)) {
                    tvCodeError.setVisibility(View.VISIBLE);
                    tvNewErrorP.setVisibility(View.GONE);
                    return false;
                } else {
                    tvNewErrorP.setVisibility(View.VISIBLE);
                    tvCodeError.setVisibility(View.VISIBLE);
                    return false;
                }
        }
        return false;
    }

    // 检查输入的 password 是否符合输入格式
    private boolean checkPwdRight(String pwd) {
        if (pwd.length() >= 8 && pwd.length() <= 16) {
            boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
            boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
            boolean flag = true;
            for (int i = 0; i < pwd.length(); i++) { //循环遍历字符串
                // Log.e("tt",Character.isDigit(str.charAt(i))+""+Character.isLetter(str.charAt(i)));
                if (Character.isDigit(pwd.charAt(i))) { //用char包装类中的判断数字的方法判断每一个字符
                    isDigit = true;
                } else if (Character.isLetter(pwd.charAt(i))) { //用char包装类中的判断字母的方法判断每一个字符
                    isLetter = true;
                } else {
                    flag = false;
                }
            }
            return flag;
        } else return false;
    }

    // todo 检验输入验证码是否正确
    private boolean checkCodeRight() {

        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.et_upd_my_set_password:
                if (hasFocus) tvSetError.setVisibility(View.INVISIBLE);
                else {
                    if (!checkPwdRight(etSetPwd.getText().toString()))
                        tvSetError.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.et_upd_my_old_password:
                if (hasFocus) tvOldError.setVisibility(View.INVISIBLE);
                else {
                    if (!checkPwdRight(etOldPwd.getText().toString()))
                        tvOldError.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.et_upd_my_password:
                if (hasFocus) tvNewErrorO.setVisibility(View.INVISIBLE);
                else {
                    if (!checkPwdRight(etNewPwdO.getText().toString()))
                        tvNewErrorO.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.et_upd_my_input_code:
                if (hasFocus) tvCodeError.setVisibility(View.INVISIBLE);
                else {
                    if (!checkCodeRight())
                        tvCodeError.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.et_upd_my_password_phone:
                if (hasFocus) tvNewErrorP.setVisibility(View.INVISIBLE);
                else {
                    if (!checkPwdRight(etNewPwdP.getText().toString()))
                        tvNewErrorP.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
