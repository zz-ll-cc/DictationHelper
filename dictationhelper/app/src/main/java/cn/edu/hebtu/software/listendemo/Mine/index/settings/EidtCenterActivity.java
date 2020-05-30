package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Login.LoginActivity;
import cn.edu.hebtu.software.listendemo.Mine.index.notify.NotifyActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.DataCleanManager;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;

import static cn.edu.hebtu.software.listendemo.Untils.Constant.DEFAULT_SLEEP_TIME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.KEEP_SLEEP_TIME;

public class EidtCenterActivity extends AppCompatActivity {
    private User user;
    private SharedPreferences sp;
    private Gson gson = new Gson();
    private ImageView ivExit;
    private RelativeLayout rlManageMsg;
    private RelativeLayout rlChangeMsg;
    private TextView tvPwdShow;
    private RelativeLayout rlShare;
    private RelativeLayout rlSuggest;
    private RelativeLayout rlAboutUs;
    private RelativeLayout rlSecret;
    private RelativeLayout rlCheck;
    private RelativeLayout rlUserDeal;
    private RelativeLayout rlChangePwd;
    private TextView tvCacheSize;
    private RelativeLayout rlCleanCache;
    private Button btnSafeOut;
    private RelativeLayout rlChangeSleep;
    private TextView tvSleepTime;
    private int sleepTime;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eidt_center);
        findViews();
        initData();
        setListeners();
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.white);
    }

    private void initData() {
        sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        user = gson.fromJson(sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        Log.e("EidtCenterActivity", "" + user.toString() + "\n" + sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER));
        tvCacheSize.setText(getCacheSize());
        if (null == user.getUpassword() || user.getUpassword().equals("") || user.getUpassword().equals("null")) {
            tvPwdShow.setText("设置密码");
        } else {
            tvPwdShow.setText("修改密码");
        }
        sleepTime = sp.getInt(KEEP_SLEEP_TIME, DEFAULT_SLEEP_TIME);
        tvSleepTime.setText(sleepTime + "秒");
    }

    private void setListeners() {
        EidtCenterListener listener = new EidtCenterListener();
        rlCleanCache.setOnClickListener(listener);
        ivExit.setOnClickListener(listener);
        btnSafeOut.setOnClickListener(listener);
        rlChangeMsg.setOnClickListener(listener);
        rlChangePwd.setOnClickListener(listener);
        rlSuggest.setOnClickListener(listener);
        rlManageMsg.setOnClickListener(listener);
        rlChangeSleep.setOnClickListener(listener);
    }

    private void findViews() {
        ivExit = findViewById(R.id.iv_edit_center_exit);
        rlManageMsg = findViewById(R.id.rl_edit_center_manage_msg);
        rlChangeMsg = findViewById(R.id.rl_edit_center_change_my_msg);
        rlChangePwd = findViewById(R.id.rl_edit_center_change_my_pwd);
        rlShare = findViewById(R.id.rl_edit_center_share);
        rlSuggest = findViewById(R.id.rl_edit_center_back_suggest);
        rlAboutUs = findViewById(R.id.rl_edit_center_about_us);
        rlSecret = findViewById(R.id.rl_edit_center_secret);
        rlCheck = findViewById(R.id.rl_edit_center_check_update);
        rlUserDeal = findViewById(R.id.rl_edit_center_user_deal);
        btnSafeOut = findViewById(R.id.btn_safe_exit);
        tvCacheSize = findViewById(R.id.tv_size_cache);
        rlCleanCache = findViewById(R.id.rl_edit_center_clean_cache);
        tvPwdShow = findViewById(R.id.tv_judge_have_pwd);
        rlChangeSleep = findViewById(R.id.rl_edit_center_change_my_sleep_time);
        tvSleepTime = findViewById(R.id.tv_edit_center_default_sleep_time);
    }

    private class EidtCenterListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_edit_center_manage_msg:
                    Intent intent5 = new Intent(EidtCenterActivity.this, NotifyActivity.class);
                    startActivity(intent5);
                    break;
                case R.id.rl_edit_center_back_suggest:
                    Intent intent = new Intent(EidtCenterActivity.this, FeedbackActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                    break;
                case R.id.btn_safe_exit:
                    AlertDialog.Builder adBuilder1 = new AlertDialog.Builder(EidtCenterActivity.this);
                    adBuilder1.setMessage("退出后请重新登陆");
                    adBuilder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(EidtCenterActivity.this, LoginActivity.class);
                            cleanSp();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            /* intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
                            // ListenIndexActivity.activity.finish();
                            startActivity(intent);
                            Toast.makeText(getApplication(), "退出成功", Toast.LENGTH_SHORT).show();
                            //finish();
                        }
                    });
                    adBuilder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 选中“取消”按钮，取消界面
                        }
                    });
                    adBuilder1.create().show();
                    break;
                case R.id.rl_edit_center_change_my_msg:
                    if (user.getUid() != 0) {
                        Intent intent1 = new Intent(EidtCenterActivity.this, EditMsgActivity.class);
                        startActivity(intent1);
                    } else {
                        Toast.makeText(EidtCenterActivity.this, "请登录/注册", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.rl_edit_center_change_my_pwd:
                    if (user.getUid() != 0) {
                        Intent intent2 = new Intent(EidtCenterActivity.this, EditPwdActivity.class);
                        startActivity(intent2);
                    } else {
                        Toast.makeText(EidtCenterActivity.this, "请登录/注册", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.iv_edit_center_exit:
                    finish();
                    break;
                case R.id.rl_edit_center_clean_cache:
                    AlertDialog.Builder adBuilder = new AlertDialog.Builder(EidtCenterActivity.this);
                    adBuilder.setTitle("确定清空数据");
                    adBuilder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cleanCache();
                            Toast.makeText(EidtCenterActivity.this, "清理成功", Toast.LENGTH_SHORT).show();
                            tvCacheSize.setText(getCacheSize());
                        }
                    });
                    adBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 选中“取消”按钮，取消界面
                        }
                    });
                    adBuilder.create().show();
                    break;
                case R.id.rl_edit_center_change_my_sleep_time:
                    showBottomDialog();
                    break;
            }
        }
    }

    private void showBottomDialog() {
        //1、使用Dialog、设置style
        dialog = new Dialog(this, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this, R.layout.custom_dialog_choose_sleep_time, null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        RelativeLayout rlEight = dialog.findViewById(R.id.rl_choose_sleep_eight);
        RelativeLayout rlTen = dialog.findViewById(R.id.rl_choose_sleep_ten);
        RelativeLayout rlFifteen = dialog.findViewById(R.id.rl_choose_sleep_fifteen);
        ImageView ivEight = dialog.findViewById(R.id.iv_choose_sleep_eight);
        ImageView ivTen = dialog.findViewById(R.id.iv_choose_sleep_ten);
        ImageView ivFifteen = dialog.findViewById(R.id.iv_choose_sleep_fifteen);
        switch (sleepTime){
            case 8:
                ivEight.setVisibility(View.VISIBLE);
                ivTen.setVisibility(View.GONE);
                ivFifteen.setVisibility(View.GONE);
                break;
            case 10:
                ivEight.setVisibility(View.GONE);
                ivTen.setVisibility(View.VISIBLE);
                ivFifteen.setVisibility(View.GONE);
                break;
            case 15:
                ivEight.setVisibility(View.GONE);
                ivTen.setVisibility(View.GONE);
                ivFifteen.setVisibility(View.VISIBLE);
                break;
        }
        rlEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSleepTime(8);
                dialog.dismiss();
            }
        });
        rlTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSleepTime(10);
                dialog.dismiss();
            }
        });
        rlFifteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSleepTime(15);
                dialog.dismiss();
            }
        });
    }


    private void changeSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        tvSleepTime.setText(sleepTime + "秒");
        sp.edit().putInt(KEEP_SLEEP_TIME, sleepTime).commit();
    }

    //获取缓存大小
    private String getCacheSize() {
        String str = "";
        try {
            str = DataCleanManager.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    //清空缓存
    private void cleanCache() {
        DataCleanManager.clearAllCache(this);
    }

    // 清空SharedPreferences数据
    private void cleanSp() {
        SharedPreferences sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(Constant.AUTO_LOGIN_KEY, false);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        setListeners();
    }
}
