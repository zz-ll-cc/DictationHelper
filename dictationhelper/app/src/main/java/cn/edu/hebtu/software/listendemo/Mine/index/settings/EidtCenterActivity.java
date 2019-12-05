package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Login.LoginActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.DataCleanManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eidt_center);
        findViews();
        initData();
        setListeners();
    }

    private void initData() {
        sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        user = gson.fromJson(sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        Log.e("EidtCenterActivity",""+user.toString());
        tvCacheSize.setText(getCacheSize());
        if (null == user.getUpassword() || user.getUpassword().equals("") || user.getUpassword().equals("null")) {
            tvPwdShow.setText("设置密码");
        } else {
            tvPwdShow.setText("修改密码");
        }
    }

    private void setListeners() {
        EidtCenterListener listener = new EidtCenterListener();
        rlCleanCache.setOnClickListener(listener);
        ivExit.setOnClickListener(listener);
        btnSafeOut.setOnClickListener(listener);
        rlChangeMsg.setOnClickListener(listener);
        rlChangePwd.setOnClickListener(listener);
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
    }

    private class EidtCenterListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_edit_center_back_suggest:
                    Intent intent = new Intent(EidtCenterActivity.this, FeedbackActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_safe_exit:
                    AlertDialog.Builder adBuilder1 = new AlertDialog.Builder(EidtCenterActivity.this);
                    adBuilder1.setMessage("退出后将清空所有数据");
                    adBuilder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(EidtCenterActivity.this, LoginActivity.class);
                            cleanCache();
                            cleanSp();
                            startActivity(intent);
                            Toast.makeText(getApplication(), "退出成功", Toast.LENGTH_SHORT).show();
                            finish();
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

            }
        }
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
        editor.clear();
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
