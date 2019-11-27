package cn.edu.hebtu.software.listendemo.Host.listenResult;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Host.learnWord.LeanTextView;
import cn.edu.hebtu.software.listendemo.R;

public class ListenResultActivity extends AppCompatActivity {
    private ListenResultRecyclerViewAdapter listenResultSuccessRecyclerViewAdapter;
    private ListenResultRecyclerViewAdapter listenResultMineRecyclerViewAdapter;
    private RecyclerView recyclerViewListenSuccess;
    private RecyclerView recyclerViewListenMine;
    private List<Map<String,Object>>  successList;
    private List<Map<String,Object>>  mineList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_result);
        initData();
        initView();

        final LeanTextView mText = findViewById(R.id.lean);
        mText.setText(Html.fromHtml("<u>"+"100"+"</u>"));
        mText.setmDegrees(20);

    }

    private void initView(){
        recyclerViewListenSuccess=findViewById(R.id.rv_success);
        recyclerViewListenMine=findViewById(R.id.rv_mine);

        listenResultSuccessRecyclerViewAdapter=new ListenResultRecyclerViewAdapter(this,successList,R.layout.activity_grade_version_recycler_item);
        listenResultMineRecyclerViewAdapter=new ListenResultRecyclerViewAdapter(this,mineList,R.layout.activity_grade_version_recycler_item);

        //设置适配器
        recyclerViewListenSuccess.setAdapter(listenResultSuccessRecyclerViewAdapter);
        recyclerViewListenMine.setAdapter(listenResultMineRecyclerViewAdapter);
        //必须调用，设置布局管理器
        recyclerViewListenSuccess.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewListenMine.setLayoutManager(new GridLayoutManager(this, 4));
    }


    private void initData() {
        String[] successs={"aaaa","bbbb","cccc","vvvv","ddddd","eeee"};
        String[] mines={"aaaa","bbbb","cccc","vvvv","ddddd","eeee"};
        long[] sid={0,1,2,3,4,5}; long[] mid={0,1,2,3,4,5};
        successList=new ArrayList<>();
        mineList=new ArrayList<>();
        for(int i=0;i<successs.length;++i){
            Map<String,Object> map=new HashMap<>();
            map.put("word",successs[i]);
            map.put("id",sid[i]);
           successList.add(map);
        }
        for(int i=0;i<mines.length;++i){
            Map<String,Object> map=new HashMap<>();
            map.put("word",mines[i]);
            map.put("id",mid[i]);
           mineList.add(map);
        }
    }
}
