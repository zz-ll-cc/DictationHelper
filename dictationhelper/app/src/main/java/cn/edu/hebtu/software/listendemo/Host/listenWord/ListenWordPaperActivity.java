package cn.edu.hebtu.software.listendemo.Host.listenWord;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.ReadManager;
import cn.edu.hebtu.software.listendemo.Untils.SmoothScrollLayoutManager;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;

import static cn.edu.hebtu.software.listendemo.Untils.Constant.DEFAULT_SLEEP_TIME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.KEEP_SLEEP_TIME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.SP_NAME;

public class ListenWordPaperActivity extends AppCompatActivity {

    private ListenWordPaperRecyclerViewAdapter listenWordPaperRecyclerViewAdapter;
    private RecyclerView recyclerViewListenWord;
    private List<Word> listenWordlist = new ArrayList<>();
    private ReadManager readManager = new ReadManager(ListenWordPaperActivity.this, "");
    private ProgressBar progressBar;
    private Button btnNext;
    private boolean flag = true;        // 是否开始重新计数
    private AutoChangeTask task;
    private int sleepTime;
    private SharedPreferences sp;
    private int trueSleepTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_word_paper);
        setTitle("听写单词");
        initData();
        initView();
        setAutoChange();
        StatusBarUtil.statusBarLightMode(this);
    }

    private void setAutoChange() {
        task = new AutoChangeTask();
        task.execute();
    }

    class AutoChangeTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            flag = true;
            progressBar.setMax(100);
            progressBar.setProgress(100);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            for (int i = 100; i >= 0; --i) {
                if (isCancelled()) return null;  //标记为取消状态时，返回null,终止任务
                try {
                    Thread.sleep(trueSleepTime);  // 50 - 10s , 100 - 15s , 30 - 8s
                    publishProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (flag = false) {
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            int nowProgress = (int) values[0];
            progressBar.setProgress(nowProgress);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            flag = false;
            btnNext.performClick();
        }
    }

    private void initView() {
        recyclerViewListenWord = findViewById(R.id.rv_listenword_paper);
        listenWordPaperRecyclerViewAdapter = new ListenWordPaperRecyclerViewAdapter(this, listenWordlist, R.layout.activity_listenword_paper_recycler_item);
        recyclerViewListenWord.setAdapter(listenWordPaperRecyclerViewAdapter);
        progressBar = findViewById(R.id.progress_listen_paper);
        SmoothScrollLayoutManager layoutManager = new SmoothScrollLayoutManager(ListenWordPaperActivity.this) {

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
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


        btnNext = findViewById(R.id.btn_next_paper);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int positionToSave = layoutManager.findFirstVisibleItemPosition();
                layoutManager.scrollToPosition(positionToSave + 1);
                View view = recyclerViewListenWord.getChildAt(0);
                if (positionToSave == listenWordlist.size() - 2) {
                    btnNext.setText("交卷");
                    // 关闭原任务，开启新任务
                    if (task.getStatus() != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                        task.cancel(true);
                    }
                    flag = false;
                    task = new AutoChangeTask();
                    task.execute();
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                int i = (int) (Math.random() * 10);
                                if (i < 5) {
                                    readManager.pronounce(listenWordlist.get(positionToSave + 1).getWenglish());
                                } else {
                                    readManager.pronounce(listenWordlist.get(positionToSave + 1).getWchinese());
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } else if (positionToSave == listenWordlist.size() - 1) {
                    Intent intent = new Intent(ListenWordPaperActivity.this, ListenResultSelectActivity.class);
                    intent.putExtra(Constant.DETAIL_PAPER, new Gson().toJson(listenWordlist));
                    startActivity(intent);
                    finish();
                    //showPopupView(v);
//                    CustomDialogListenWord dialog=new CustomDialogListenWord(listenWordlist,mineWordlist, ListenWordPaperActivity.this);
//                    dialog.setCancelable(false);
//                    dialog.show(getSupportFragmentManager(),"listen");
                } else {
                    // 关闭原任务，开启新任务
                    if (task.getStatus() != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                        task.cancel(true);
                    }
                    flag = false;
                    task = new AutoChangeTask();
                    task.execute();
                    //延迟播放
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                int i = (int) (Math.random() * 10);
                                if (i < 5) {
                                    readManager.pronounce(listenWordlist.get(positionToSave + 1).getWenglish());
                                } else {
                                    readManager.pronounce(listenWordlist.get(positionToSave + 1).getWchinese());
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
        readManager.pronounce(listenWordlist.get(0).getWenglish());

    }


    private void initData() {
        Intent intent = getIntent();
        String str = intent.getStringExtra(Constant.DETAIL_CON_RECITE_OR_DICTATION);
        if (str != null && !str.equals("")) {
            Log.e("tt", str);
            Type listType = new TypeToken<List<Word>>() {
            }.getType();
            listenWordlist = new Gson().fromJson(str, listType);
        }
        String str1 = intent.getStringExtra(Constant.RECITE_CON_DICTATION);
        if (str1 != null && !str1.equals("")) {
            Type listType = new TypeToken<List<Word>>() {
            }.getType();
            Log.e("tt", str1);
            listenWordlist = new Gson().fromJson(str1, listType);
        }
        String str2 = intent.getStringExtra(Constant.NEWWORD_CON_LEARNWORD_DICTATION);
        if (str2 != null) {
            Type listType = new TypeToken<List<Word>>() {
            }.getType();
            listenWordlist = new Gson().fromJson(str2, listType);
            str1 = null;
        }
        sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        sleepTime = sp.getInt(KEEP_SLEEP_TIME, DEFAULT_SLEEP_TIME);
        switch (sleepTime) {
            case 8:
                trueSleepTime = 30;
                break;
            case 10:
                trueSleepTime = 50;
                break;
            case 15:
                trueSleepTime = 100;
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task.getStatus() != null && task.getStatus() == AsyncTask.Status.RUNNING) {
            task.cancel(true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
            adBuilder.setTitle("是否退出本次听写");
            adBuilder.setMessage("退出后本次听写将不做记录");
            adBuilder.setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 选中“确定”按钮，解除绑定
                    // 更改SharedP中数据
                    finish();
                    if (task.getStatus() != null && task.getStatus() == AsyncTask.Status.RUNNING) {
                        task.cancel(true);
                    }
                }
            });
            adBuilder.setNegativeButton("我手滑了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 选中“取消”按钮，取消界面

                }
            });
            adBuilder.create().show();
        }
        return false;
    }
}

