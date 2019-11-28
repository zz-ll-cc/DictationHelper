package cn.edu.hebtu.software.listendemo.Host.listenWord;

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
import android.widget.EditText;
import android.widget.PopupWindow;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.listenResult.ListenResultActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

public class ListenWordActivity extends AppCompatActivity {

    private ListenWordRecyclerViewAdapter listenWordRecyclerViewAdapter;
    private RecyclerView recyclerViewListenWord;
    private List<Word>  listenWordlist;
    private List<Word> mineWordlist=new ArrayList<>();
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
        final Button btnNext=findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView.LayoutManager layoutManager = recyclerViewListenWord.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    int postion=firstItemPosition+1;
                    if(firstItemPosition<listenWordlist.size()){
                        EditText editText=findViewById(R.id.et_word);
                        Word word=new Word();
                        String str=editText.getText().toString();
                        word.setWenglish(str);
                        mineWordlist.add(word);
                        recyclerViewListenWord.scrollToPosition(postion);
                    }
                    if(postion==listenWordlist.size()-1){
                        btnNext.setText("交卷");
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
        String str=intent.getStringExtra(Constant.DETAIL_CON_RECITE_OR_DICTATION);
        if(str!=null && !str.equals("")){
            Type listType=new TypeToken< List<Word> >(){}.getType();
            listenWordlist=new Gson().fromJson(str,listType);
        }
        String str1=intent.getStringExtra(Constant.RECITE_CON_DICTATION);
        if(str1!=null && !str1.equals("")){
            Type listType=new TypeToken<List<Word>>(){}.getType();
            listenWordlist=new Gson().fromJson(str1,listType);
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
                intent.putExtra("success",new Gson().toJson(listenWordlist));
                intent.putExtra("mine",new Gson().toJson(mineWordlist));
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
        popupWindow.showAtLocation(v, Gravity.CENTER,0,20);
    }

    //  将物理像素装换成真实像素
    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}

