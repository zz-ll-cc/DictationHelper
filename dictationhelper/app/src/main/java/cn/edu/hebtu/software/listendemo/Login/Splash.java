package cn.edu.hebtu.software.listendemo.Login;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.hebtu.software.listendemo.Host.index.ListenIndexActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;

public class Splash extends AppCompatActivity {


    @BindView(R.id.btn_skip)
    Button btnSkip;
    private SharedPreferences sp;
    private int jumpType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(1500);//使程序休眠五秒

                    if(jumpType == -1){
                        return;
                    }
                    if(jumpType == 1){
                        Intent intent = new Intent(getApplicationContext(), ListenIndexActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent it = new Intent(getApplicationContext(), LoginActivity.class);//启动MainActivity
                        startActivity(it);
                        finish();//关闭当前活动
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        if (sp.getBoolean(Constant.AUTO_LOGIN_KEY, Constant.DEFAULT_LOGIN_KEY)) {
            // 此时不为第一次登陆或退出登录情况，自动登陆
            jumpType = 1;
        } else {

        }
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myThread.isAlive()){
                    jumpType=-1;
                    if(jumpType == 1){
                        Intent intent = new Intent(getApplicationContext(), ListenIndexActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent it = new Intent(getApplicationContext(), LoginActivity.class);//启动MainActivity
                        startActivity(it);
                        finish();//关闭当前活动
                    }
                }
            }
        });
        StatusBarUtil.transparencyBar(this);
        myThread.start();//启动线程
    }
}