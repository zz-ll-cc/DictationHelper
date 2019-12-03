package cn.edu.hebtu.software.listendemo.Login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import cn.edu.hebtu.software.listendemo.Entity.PostRegisterUser;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.OkHttpClient;

public class RegisterPhoneFragment extends Fragment {
    private static final int CHECK_CAN_REGISTER = 1;
    private View view;
    private ImageView ivBack;
    private TextView tvSla;
    private TextView tvSecret;
    private EditText etPhone;
    private ImageView ivPhoneClean;
    private TextView tvPhoneError;
    private EditText etPassword;
    private ImageView ivPasswordClean;
    private EditText etRePassword;
    private ImageView ivRePasswordClean;
    private TextView tvReWrongMsg;
    private Button btnNext;
    private RegisterPhoneListener listener = new RegisterPhoneListener();
    private PostRegisterUser user = new PostRegisterUser();
    private boolean canRegister = false;
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHECK_CAN_REGISTER:
                    canRegister = gson.fromJson(msg.obj.toString(), boolean.class);
                    if (canRegister) {
                        btnNext.setEnabled(true);
                        tvPhoneError.setVisibility(View.GONE);
                    } else {
                        btnNext.setEnabled(false);
                        tvPhoneError.setVisibility(View.VISIBLE);
                        tvPhoneError.setText("用户已被注册");
                    }
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_phone, container, false);
        findViews();
        setListener();
        etPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 当获取焦点时，不可注册文本信息隐藏
                    tvPhoneError.setVisibility(View.GONE);
                    canRegister = false;
                    btnNext.setEnabled(false);
                } else {
                    if (etPhone.getText().toString().equals("")) {
                        tvPhoneError.setVisibility(View.VISIBLE);
                        tvPhoneError.setText("请输入正确的手机号");
                        canRegister = false;
                        btnNext.setEnabled(false);
                    } else {
//                    FormBody fb = new FormBody.Builder()
//                            .add("",etPhone.getText().toString())
//                            .build();
//                    Request request = new Request.Builder()
//                            .url(Constant.URL_CAN_REGISTER)
//                            .post(fb).build();
//                    Call call = client.newCall(request);
//                    call.enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
                        Message message = new Message();
                        message.what = CHECK_CAN_REGISTER;
                        message.obj = gson.toJson(true);
                        handler.sendMessage(message);
//                        }
//                    });
                    }
                }
            }
        });
        return view;
    }

    private void setListener() {
        ivBack.setOnClickListener(listener);
        ivPhoneClean.setOnClickListener(listener);
        ivPasswordClean.setOnClickListener(listener);
        ivRePasswordClean.setOnClickListener(listener);
        btnNext.setOnClickListener(listener);
    }

    private void findViews() {
        ivBack = view.findViewById(R.id.iv_login_register_phone_exit);
        tvSla = view.findViewById(R.id.tv_login_regist_read_sla);
        tvSecret = view.findViewById(R.id.tv_login_regist_read_secret);
        etPhone = view.findViewById(R.id.et_login_regist_phone);
        ivPhoneClean = view.findViewById(R.id.iv_login_regist_clean_phone);
        tvPhoneError = view.findViewById(R.id.tv_regist_phone_error);
        etPassword = view.findViewById(R.id.et_login_regist_password);
        ivPasswordClean = view.findViewById(R.id.iv_login_regist_clean_password);
        etRePassword = view.findViewById(R.id.et_login_regist_re_password);
        ivRePasswordClean = view.findViewById(R.id.iv_login_regist_clean_re_password);
        tvReWrongMsg = view.findViewById(R.id.tv_regist_re_pwd_error);
        btnNext = view.findViewById(R.id.btn_regist_next);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class RegisterPhoneListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_login_register_phone_exit:
                    // 结束注册界面
                    getActivity().finish();
                    break;
                case R.id.tv_login_regist_read_sla:
                    break;
                case R.id.tv_login_regist_read_secret:
                    break;
                case R.id.iv_login_regist_clean_phone:
                    etPhone.setText("");
                    break;
                case R.id.iv_login_regist_clean_password:
                    etPassword.setText("");
                    break;
                case R.id.iv_login_regist_clean_re_password:
                    etRePassword.setText("");
                case R.id.btn_regist_next:
                    String password = etPassword.getText().toString();
                    String rePassword = etRePassword.getText().toString();
                    if (password.equals("") || rePassword.equals("")) {
                        if (rePassword.equals("") && !password.equals("")) {
                            tvReWrongMsg.setVisibility(View.VISIBLE);
                            tvReWrongMsg.setText("请重新输入密码！");
                        } else {
                            tvReWrongMsg.setVisibility(View.VISIBLE);
                            tvReWrongMsg.setText("请输入密码！");
                        }
                    } else if (!password.equals(rePassword)) {
                        tvReWrongMsg.setVisibility(View.VISIBLE);
                        tvReWrongMsg.setText("两次密码输入不一致！");
                    } else {
                        tvPhoneError.setVisibility(View.GONE);
                        tvReWrongMsg.setVisibility(View.GONE);
                        user.setStepId(Constant.REGISTER_FRAGMENT_STEP_ONE_ID);
                        User ruser = new User();
                        ruser.setUphone(etPhone.getText().toString());
                        ruser.setUpassword(etPassword.getText().toString());
                        user.setUser(ruser);
                        EventBus.getDefault().post(user);
                    }
                    break;
            }
        }
    }
}
