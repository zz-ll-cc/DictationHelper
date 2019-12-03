package cn.edu.hebtu.software.listendemo.Host.learnWord;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.listenWord.ListenWordActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

public class LearnWordActivity extends AppCompatActivity {

    private LearnWordRecyclerViewAdapter learnWordRecyclerViewAdapter;
    private RecyclerView recyclerViewLearnWord;
    private List<Word> learnWordlist;
    private int i = 0;
    private PopupWindow popupWindow = null;
    private View popupView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_word);
        setTitle("学习单词");
        initData();
        initView();
    }

    private void initView() {
        recyclerViewLearnWord= findViewById(R.id.rv_learnword);
        learnWordRecyclerViewAdapter = new LearnWordRecyclerViewAdapter(this, learnWordlist, R.layout.activity_learnword_recycler_item);
        recyclerViewLearnWord.setAdapter(learnWordRecyclerViewAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerViewLearnWord.setLayoutManager(linearLayoutManager);
        final Button btnNext = findViewById(R.id.btn_next);
        Button btnPrevious=findViewById(R.id.btn_previous);
        final RecyclerView.LayoutManager layoutManager = recyclerViewLearnWord.getLayoutManager();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    int postion=firstItemPosition+1;
                    if (firstItemPosition < learnWordlist.size() - 1) {
                        recyclerViewLearnWord.scrollToPosition(firstItemPosition + 1);
                    }
                    if(postion==learnWordlist.size()-1){
                        btnNext.setText("去听写");
                    }
                    if(firstItemPosition==learnWordlist.size()-1){
                        showPopupView(v);
                    }
                }
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    Log.e("firstItemPosition",firstItemPosition+"");
                    if (firstItemPosition >0) {
                        recyclerViewLearnWord.scrollToPosition(firstItemPosition - 1);
                    }else{
                        Toast.makeText(LearnWordActivity.this,"已经是第一个了",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }


    private void initData() {
        Intent intent=getIntent();
        String str=intent.getStringExtra(Constant.DETAIL_CON_RECITE_OR_DICTATION);
        if(str!=null){
            Type listType=new TypeToken< List<Word> >(){}.getType();
            learnWordlist=new Gson().fromJson(str,listType);
        }
    }

    private void showPopupView(View v) {
        popupView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_finishlearn, null);
        popupWindow = new PopupWindow(popupView, dip2px(this, 300), dip2px(this, 200), true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        Button btnOk = popupView.findViewById(R.id.btn_OK);
        Button btnCancel = popupView.findViewById(R.id.btn_cancle);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson=new Gson();
                Intent intent =new Intent(LearnWordActivity.this,ListenWordActivity.class);
                intent.putExtra(Constant.RECITE_CON_DICTATION,gson.toJson(learnWordlist));
                startActivity(intent);
                popupWindow.dismiss();
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    //  将物理像素装换成真实像素
    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}


