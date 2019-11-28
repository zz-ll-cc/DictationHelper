package cn.edu.hebtu.software.listendemo.Host.index;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Mine.index.MyInfoFragment;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Record.index.RecordFragment;

public class ListenIndexActivity  extends AppCompatActivity {
    //所需要的全部资源
    private class MyTabSpec {
        private TextView textView = null;
        private int normalImage;
        private int selectImage;
        private Fragment fragment = null;

        //set get方法
        public TextView getTextView() {
            return textView;
        }

        public int getNormalImage() {
            return normalImage;
        }

        public int getSelectImage() {
            return selectImage;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public void setNormalImage(int normalImage) {
            this.normalImage = normalImage;
        }

        public void setSelectImage(int selectImage) {
            this.selectImage = selectImage;
        }

        public void setFragment(Fragment fragment) {
            this.fragment = fragment;
        }

        //图片以及文字选中未选中状态的改变
        private void setSelect(boolean b) {
            if (b) {
                textView.setTextColor(getResources().getColor(R.color.colorAccent));
            } else {
                textView.setTextColor(Color.parseColor("#000000"));
            }
        }
    }

    //字符串与MyTabSpec进行匹配   值：MytabSpec对象   键：字符串
    private Map<String, MyTabSpec> map = new HashMap<>();
    private String[] tabStrid = {"首页", "学习记录", "我的"};
    //用于记录当前正在显示的Fragment
    private Fragment curFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_index);
        Intent intent=getIntent();
        initDtata(); //初始化MyTabSpec
        changeTab(tabStrid[0]);  //设置默认显示的TabSpec

    }

    //设置TabHost点击事件
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_index:
                changeTab(tabStrid[0]);
                break;
            case R.id.ll_record:
                changeTab(tabStrid[1]);
                break;
            case R.id.ll_me:
                changeTab(tabStrid[2]);
                break;
        }
    }

    //根据Tab ID 切换Tab
    private void changeTab(String s) {
        changeFragment(s); // 切换Fragment
        changeImage(s);  //  切换图标 及字体颜色
    }

    //根据Tab ID 切换Fragment
    private void changeFragment(String s) {
        Fragment fragment = map.get(s).getFragment();
        if (curFragment == fragment) {
            return;
        }
        // Fragment事务- Fragment事务管理器
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //将之前显示的Fragment隐藏掉
        if (curFragment != null) {
            transaction.remove(curFragment);
            //transaction.hide(curFragment);
        }
        //如果当前Fragment没有被添加过，则添加到Activity的帧布局中
        if (!fragment.isAdded()) {
            transaction.add(R.id.tab_content, fragment);
        }
        //显示对应Fragment
        transaction.show(fragment);
        curFragment = fragment;

        transaction.commit();
    }

    //根据Tab ID 切换Tab显示的图片及字体颜色
    private void changeImage(String s) {
        for (String key : map.keySet()) {
            map.get(key).setSelect(false); // 所有的Tab的图片 字体恢复默认
        }
        map.get(s).setSelect(true); //设置选中的Tab的图片和字体颜色
    }

    //初始化MyTabSpec
    private void initDtata() {
        // 1 创建MyTabSpec对象
        map.put(tabStrid[0], new MyTabSpec());
        map.put(tabStrid[1], new MyTabSpec());
        map.put(tabStrid[2], new MyTabSpec());
        setFragment();   // 设置Fragment
        findView();      // 设置ImageView 和 TextView
    }

    //创建Fragment对象 并放入map中的MyTabSpec对象
    private void setFragment() {
        map.get(tabStrid[0]).setFragment(new HostFragment());
        map.get(tabStrid[1]).setFragment(new RecordFragment());
        map.get(tabStrid[2]).setFragment(new MyInfoFragment());
    }

    //将ImageView和TextView放入map的MyTabSpec对象
    private void findView() {
        TextView tv1 = findViewById(R.id.tv_index);
        TextView tv2 = findViewById(R.id.tv_record);
        TextView tv3 = findViewById(R.id.tv_me);
        map.get(tabStrid[0]).setTextView(tv1);
        map.get(tabStrid[1]).setTextView(tv2);
        map.get(tabStrid[2]).setTextView(tv3);

    }


}
