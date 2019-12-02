package cn.edu.hebtu.software.listendemo.Login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.PostRegisterUser;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

public class RegistActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Fragment curFragment;
    private Map<String, RegisterSpec> map = new HashMap<>();
    private String[] registerFragmentId = {Constant.REGISTER_FRAGMENT_STEP_ONE_ID, Constant.REGISTER_FRAGMENT_STEP_TWO_ID};
    private User registerUser = new User();
    private String initRegisterFragmentId = Constant.REGISTER_FRAGMENT_STEP_ONE_ID;
    private String nowFragmentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        EventBus.getDefault().register(this);

        progressBar = findViewById(R.id.pb_register_process);
        initData();
        changeTab(initRegisterFragmentId);
    }

    private void changeTab(String fragmentId) {
        // 1. 切换成对应的 fragment
        Fragment fragment = map.get(fragmentId).getFragment();
        if (fragment == curFragment) return;  // 判断选中的 tabspec 是否为当前显示的
        // 获取 Fragment 事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 隐藏之前显示的fragment
        if (curFragment != null)
            transaction.hide(curFragment);
        // 若当前Fragment没有被添加过，则添加到Activity中的帧布局中
        if (!fragment.isAdded()) {
            transaction.add(R.id.fl_register, fragment);
        }
        // 显示对应Fragment
        transaction.show(fragment);
        nowFragmentId = fragmentId;
        curFragment = fragment;
        transaction.commit();
        // 2. 修改processBar显示
        switch (nowFragmentId) {
            case Constant.REGISTER_FRAGMENT_STEP_ONE_ID:
                map.get(nowFragmentId).changeProgress(Constant.REGISTER_STEP_ONE);
                break;
            case Constant.REGISTER_FRAGMENT_STEP_TWO_ID:
                map.get(nowFragmentId).changeProgress(Constant.REGISTER_STEP_TWO);
                break;
        }
    }

    private void initData() {
        RegisterSpec spec1 = new RegisterSpec();
        spec1.setFragment(new RegisterPhoneFragment());
        spec1.setProgressBar(progressBar);

        RegisterSpec spec2 = new RegisterSpec();
        spec2.setFragment(new RegisterMsgFragment());
        spec2.setProgressBar(progressBar);

        map.put(registerFragmentId[0], spec1);
        map.put(registerFragmentId[1], spec2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void backToStepOne(String stepId){
        changeTab(stepId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRegisterUser(PostRegisterUser user) {
        switch (user.getStepId()) {
            case Constant.REGISTER_FRAGMENT_STEP_ONE_ID:
                registerUser.setUphone(user.getUser().getUphone());
                registerUser.setUpassword(user.getUser().getUpassword());
                changeTab(Constant.REGISTER_FRAGMENT_STEP_TWO_ID);
                break;
            case Constant.REGISTER_FRAGMENT_STEP_TWO_ID:
                registerUser.setUbirth(user.getUser().getUbirth());
                registerUser.setUcity(user.getUser().getUcity());
                registerUser.setUheadPath(user.getUser().getUheadPath());
                registerUser.setVip(user.getUser().getVip());
                registerUser.setUname(user.getUser().getUname());

                PostRegisterUser registerUserPost = new PostRegisterUser();
                registerUserPost.setUser(registerUser);
                registerUserPost.setStepId(Constant.REGISTER_FINISH);
                EventBus.getDefault().post(registerUserPost);
                break;
        }
    }
}
