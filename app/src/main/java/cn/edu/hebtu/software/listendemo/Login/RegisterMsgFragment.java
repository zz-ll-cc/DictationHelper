package cn.edu.hebtu.software.listendemo.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.edu.hebtu.software.listendemo.Entity.PostRegisterUser;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Host.index.ListenIndexActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.OkHttpClient;

public class RegisterMsgFragment extends Fragment {
    private static final int REGISTER_FINISH = 1;
    private View view;
    private ImageView ivBack;
    private EditText etName;
    private EditText etCity;
    private EditText etBirth;
    private TextView tvWrongMsg;
    private Button btnFinAndLogin;
    private PostRegisterUser user = new PostRegisterUser();
    private Gson gson = new Gson();
    private OkHttpClient client = new OkHttpClient();
    private RegisterMsgListener listener = new RegisterMsgListener();
    private SharedPreferences sp = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REGISTER_FINISH:
                    Intent intent = new Intent(getContext(), ListenIndexActivity.class);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean(Constant.AUTO_LOGIN_KEY, true);
                    editor.putString(Constant.USER_KEEP_KEY, gson.toJson(user.getUser()));
                    startActivity(intent);
                    Toast.makeText(getContext(), "欢迎登陆" + user.getUser().getUname() + "!", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_user_msg, container, false);
        EventBus.getDefault().register(this);
        sp = getActivity().getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        findViews();
        ivBack.setOnClickListener(listener);
        btnFinAndLogin.setOnClickListener(listener);
        return view;
    }

    private void findViews() {
        ivBack = view.findViewById(R.id.iv_login_regist_user_msg_exit);
        etName = view.findViewById(R.id.et_login_regist_username);
        etCity = view.findViewById(R.id.et_login_regist_city);
        etBirth = view.findViewById(R.id.et_login_regist_birth);
        tvWrongMsg = view.findViewById(R.id.tv_login_regist_username_error);
        btnFinAndLogin = view.findViewById(R.id.btn_regist_finish);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void registerUser(PostRegisterUser registerUser) {
        switch (registerUser.getStepId()) {
            case Constant.REGISTER_FINISH:
                // TODO: 2019/11/30 执行注册URL操作
                Message message = new Message();
                message.what = REGISTER_FINISH;
                message.obj = gson.toJson(true);
                handler.sendMessage(message);
                break;
        }
    }

    private class RegisterMsgListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_login_regist_user_msg_exit:
                    EventBus.getDefault().post(Constant.REGISTER_FRAGMENT_STEP_ONE_ID);
                    etBirth.setText("");
                    etName.setText("");
                    etCity.setText("");
                    break;
                case R.id.btn_regist_finish:
                    if (!etName.getText().toString().equals("")) {
                        // 当可以注册时，构造创建的 user 对象
                        user.setStepId(Constant.REGISTER_FRAGMENT_STEP_TWO_ID);
                        User userMsg = new User();
                        userMsg.setUname(etName.getText().toString());
                        userMsg.setUheadPath("");
                        userMsg.setUcity(etCity.getText().toString());
                        userMsg.setUbirth(etBirth.getText().toString());
                        user.setUser(userMsg);
                        EventBus.getDefault().post(user);
                    }else{
                        tvWrongMsg.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }
}
