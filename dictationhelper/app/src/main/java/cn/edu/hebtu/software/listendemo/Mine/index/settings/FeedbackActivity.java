package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        StatusBarUtil.statusBarLightMode(this);
    }
}
