package cn.edu.hebtu.software.listendemo.Host.listenWord;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import cn.edu.hebtu.software.listendemo.Untils.ReadManager;
import cn.edu.hebtu.software.listendemo.Untils.SmoothScrollLayoutManager;

public class ListenWordActivity extends AppCompatActivity {

    private ListenWordRecyclerViewAdapter listenWordRecyclerViewAdapter;
    private RecyclerView recyclerViewListenWord;
    private List<Word>  listenWordlist;
    private List<Word> mineWordlist=new ArrayList<>();
    private int i=0;
    private PopupWindow popupWindow=null;
    private View popupView=null;
    private ReadManager readManager = new ReadManager(ListenWordActivity.this,"");

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

        SmoothScrollLayoutManager layoutManager = new SmoothScrollLayoutManager(ListenWordActivity.this){

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerViewListenWord.setLayoutManager(layoutManager);

        recyclerViewListenWord.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                /*
                new state 有三种状态
                SCROLL_STATE_IDLE = 0 静止没有滚动
                SCROLL_STATE_DRAGGING = 1  用户正在拖拽
                SCROLL_STATE_SETTLING = 2 自动滚动

                 */

//                if(newState == 0){
//                    if(lastPage != layoutManager.findFirstVisibleItemPosition()){
//                        readManager.pronounce(listenWordlist.get(lastPage+1).getWenglish());
//
//                    }
//                    Log.e("findFirst",""+layoutManager.findFirstVisibleItemPosition());
//                    lastPage = layoutManager.findFirstVisibleItemPosition();
//                }
//


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*
                    recyclerView : 当前滚动的view
                    dx : 水平滚动距离
                    dy : 垂直滚动距离

                    dx > 0 时为手指向左滚动,列表滚动显示右面的内容
                    dx < 0 时为手指向右滚动,列表滚动显示左面的内容
                    dy > 0 时为手指向上滚动,列表滚动显示下面的内容
                    dy < 0 时为手指向下滚动,列表滚动显示上面的内容

                 */

            }
        });


        final Button btnNext=findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("firstVisible",""+layoutManager.findFirstCompletelyVisibleItemPosition());
                int positionToSave = layoutManager.findFirstVisibleItemPosition();
//                layoutManager.smoothScrollToPosition(recyclerViewListenWord,new RecyclerView.State(),positionToSave+1);
                layoutManager.scrollToPosition(positionToSave+1);
                View view = recyclerViewListenWord.getChildAt(0);
                EditText editText = view.findViewById(R.id.et_word);
                mineWordlist.get(positionToSave).setWenglish(editText.getText().toString());
                Log.e("word",""+editText.getText().toString());
                if(positionToSave == listenWordlist.size()-2){
                    btnNext.setText("交卷");
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                readManager.pronounce(listenWordlist.get(positionToSave+1).getWenglish());
                                Log.e("text","pronounce");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }else if(positionToSave == listenWordlist.size()-1){
                    showPopupView(v);
                    btnNext.setVisibility(View.INVISIBLE);
                }else{
//                    延迟播放
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    readManager.pronounce(listenWordlist.get(positionToSave+1).getWenglish());
                                    Log.e("text","pronounce");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                }
//                RecyclerView.LayoutManager layoutManager = recyclerViewListenWord.getLayoutManager();
//                if (layoutManager instanceof LinearLayoutManager) {
//                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//                    //获取第一个可见view的位置
//                    final int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//
//                    int postion=firstItemPosition+1;
//                    if(firstItemPosition<listenWordlist.size()){
//                        EditText editText=findViewById(R.id.et_word);
//                        Word word=new Word();
//                        String str=editText.getText().toString();
//                        word.setWenglish(str);
//                        mineWordlist.add(word);
//                        recyclerViewListenWord.scrollToPosition(postion);
//
//                        //延迟播放
//                        new Thread(){
//                            @Override
//                            public void run() {
//                                try {
//                                    Thread.sleep(1000);
//                                    readManager.pronounce(listenWordlist.get(firstItemPosition+1).getWenglish());
//                                    Log.e("text","pronounce");
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }.start();
//                    }
//                    if(postion==listenWordlist.size()-1){
//                        btnNext.setText("交卷");
//                    }
//                    if(firstItemPosition==listenWordlist.size()-1){
//                        showPopupView(v);
//                    }
//                }
            }
        });
        readManager.pronounce(listenWordlist.get(0).getWenglish());

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
        for(Word word : listenWordlist){
            Word w = new Word();
            mineWordlist.add(w);
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

