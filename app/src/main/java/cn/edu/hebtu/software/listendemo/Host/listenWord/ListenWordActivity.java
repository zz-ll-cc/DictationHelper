package cn.edu.hebtu.software.listendemo.Host.listenWord;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Host.listenResult.ListenResultActivity;
import cn.edu.hebtu.software.listendemo.R;

public class ListenWordActivity extends AppCompatActivity {

    private ListenWordRecyclerViewAdapter listenWordRecyclerViewAdapter;
    private RecyclerView recyclerViewListenWord;
    private List<Map<String,Object>>  listenWordlist;
    private int i=0;
    private PopupWindow popupWindow=null;
    private View popupView=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_word);
        setTitle("听写单词");
        initData();
        initView();
    }

    private void initView(){
        recyclerViewListenWord=findViewById(R.id.rv_listenword);
        listenWordRecyclerViewAdapter=new ListenWordRecyclerViewAdapter(this,listenWordlist,R.layout.activity_listenword_recycler_item);
        recyclerViewListenWord.setAdapter(listenWordRecyclerViewAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerViewListenWord.setLayoutManager(linearLayoutManager);
        Button btnNext=findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView.LayoutManager layoutManager = recyclerViewListenWord.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if(firstItemPosition<listenWordlist.size()-1){
                        recyclerViewListenWord.scrollToPosition(firstItemPosition+1);
                    }
                    if(firstItemPosition==listenWordlist.size()-1){
                        showPopupView(v);
                    }
                }
            }
        });

    }


    private void initData() {
        Intent intent=getIntent();
        String str=intent.getStringExtra("wordlist");
        if(str!=null){
            Type listType=new TypeToken< List<Map<String, Object>> >(){}.getType();
            listenWordlist=new Gson().fromJson(str,listType);
        }else{
            String[] words={"a","b","c","d","e","f"};
            long[] wid={0,1,2,3,4,5};
            listenWordlist=new ArrayList<>();
            for(int i=0;i<words.length;++i){
                Map<String,Object> map=new HashMap<>();
                map.put("word",words[i]);
                map.put("id",wid[i]);
                listenWordlist.add(map);
            }
        }
    }

    private void showPopupView(View v) {
        popupView= LayoutInflater.from(this).inflate(R.layout.custom_dialog_finishlisten,null);
        popupWindow=new PopupWindow(popupView, dip2px(this,300) , dip2px(this,200), true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setTouchable(true);
        Button btnOk=popupView.findViewById(R.id.btn_OK);
        Button btnCancel=popupView.findViewById(R.id.btn_cancle);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ListenWordActivity.this,ListenResultActivity.class);
                startActivity(intent);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
    }

    //  将物理像素装换成真实像素
    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}

