package cn.edu.hebtu.software.listendemo.Host.listenWord;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Entity.WrongWord;
import cn.edu.hebtu.software.listendemo.Host.index.ListenIndexActivity;
import cn.edu.hebtu.software.listendemo.Host.listenResult.ListenResultActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.CorrectSumDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.CorrectSumMonthDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.CorrectWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import cn.edu.hebtu.software.listendemo.Untils.WrongWordDBHelper;
import cn.edu.hebtu.software.listendemo.credit.Utils.Const;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListenResultSelectActivity extends AppCompatActivity {
    private TextView tvRreturnListenSelect;
    private RecyclerView rvResultPaperSelect;
    private List<Word> paperlist = new ArrayList<>();
    private Map<Integer, Boolean> checkStatus;//用来记录所有checkbox的状态
    private List<Word> listenWordlist;
    private List<WrongWord> wrongWordList = new ArrayList<>();
    private SQLiteDatabase database;

    private SQLiteDatabase currectdatabase;
    private SQLiteDatabase currectsumdatabase;
    private SQLiteDatabase currectsumMonthdatabase;
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
    public SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
    public SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM");// HH:mm:ss
    private Date date;
    private int sum;
    private int error = 0;
    private int correct = 0;
    private double score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_listen_select);
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this,R.color.bar_result_page);
        initView();
        initData();
        ListenResultSelectRecyclerViewAdapter listenResultSelectRecyclerViewAdapter = new ListenResultSelectRecyclerViewAdapter(this, paperlist, R.layout.activity_paper_listen_select_item, checkStatus);
        rvResultPaperSelect.setAdapter(listenResultSelectRecyclerViewAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvResultPaperSelect.setLayoutManager(layoutManager);
        tvRreturnListenSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNULL = false;
                for (int j = 0; j < paperlist.size(); j++) {
                    if (checkStatus.get(j) == null) {
                        isNULL = true;
                    }
                }
                if (isNULL == false) {
                    sum = paperlist.size();
                    date = new Date(System.currentTimeMillis());
                    Log.e("tttttttttt", checkStatus.toString());
                    for (int i = 0; i < paperlist.size(); i++) {
                        if(checkStatus.get(i)==true){
                            correct++;
                        }
                        if(checkStatus.get(i)==false){
                            error++;
                        }
                        score = (sum - error) / (sum * 1.0);
                        Word w1 = paperlist.get(i);
                        Word w = paperlist.get(i);
                        if (checkStatus.get(i) == true) {
                            Cursor cursor1 = currectdatabase.query("TBL_CURRECTWORD", null, "WENGLISH=?", new String[]{w1.getWenglish()}, null, null, null);
                            Log.e("currectnum", cursor1.getCount() + "");
                            if (cursor1.getCount() == 0) {
                                //添加
                                ContentValues word = new ContentValues();
                                word.put("WENGLISH", w1.getWenglish());
                                word.put("WCHINESE", w1.getWchinese());
                                word.put("WIMGPATH", w1.getWimgPath());
                                word.put("UNID", w1.getUnid());
                                word.put("BID", w1.getBid());
                                word.put("TYPE", w1.getType());
                                word.put("ISTRUE", w1.getIsTrue());
                                word.put("ADDTIME", simpleDateFormat1.format(date));
                                long row = currectdatabase.insert("TBL_CURRECTWORD", null, word);
                                //                    Log.e("插入正确词的行号", row + "");
                            }

                            Cursor cursor2 = currectsumdatabase.query("TBL_CURRECTSUM", null, "ADDTIME=?", new String[]{simpleDateFormat1.format(date)}, null, null, null);
                            Log.e("currectsumnum", cursor2.getCount() + "");
                            if (cursor2.getCount() == 0) {
                                //添加
                                ContentValues word = new ContentValues();
                                word.put("SUM", correct);
                                word.put("ADDTIME", simpleDateFormat1.format(date));
                                long row = currectsumdatabase.insert("TBL_CURRECTSUM", null, word);
                                //                    Log.e("插入正确词总数的行号", row + "");
                            }
                            if (cursor2.getCount() == 1) {
                                if (cursor2.moveToFirst()) {
                                    int sum = cursor2.getInt(cursor2.getColumnIndex("SUM"));
                                    ContentValues word = new ContentValues();
                                    word.put("SUM", correct + sum);
                                    word.put("ADDTIME", simpleDateFormat1.format(date));
                                    currectsumdatabase.update("TBL_CURRECTSUM", word, "ADDTIME=?", new String[]{simpleDateFormat1.format(date)});
                                }
                                while (cursor2.moveToNext()) ;
                            }

                            Cursor cursor3 = currectsumMonthdatabase.query("TBL_CURRECTSUMMONTH", null, "ADDTIME=?", new String[]{simpleDateFormat2.format(date)}, null, null, null);
                            if (cursor3.getCount() == 0) {
                                //添加
                                ContentValues word = new ContentValues();
                                word.put("SUM", correct);
                                word.put("ADDTIME", simpleDateFormat2.format(date));
                                long row = currectsumMonthdatabase.insert("TBL_CURRECTSUMMONTH", null, word);
                                Log.e("插入月正确词总数的行号", row + "");
                            }
                            if (cursor3.getCount() == 1) {
                                if (cursor3.moveToFirst()) {
                                    int summ = cursor3.getInt(cursor3.getColumnIndex("SUM"));
                                    ContentValues word = new ContentValues();
                                    word.put("SUM", correct + summ);
                                    word.put("ADDTIME", simpleDateFormat2.format(date));
                                    currectsumMonthdatabase.update("TBL_CURRECTSUMMONTH", word, "ADDTIME=?", new String[]{simpleDateFormat2.format(date)});
                                }
                                while (cursor3.moveToNext()) ;
                            }
                        } else {
                            Cursor cursor = database.query("TBL_WRONGWORD", null, "WENGLISH=?", new String[]{w.getWenglish()}, null, null, null);
                            //                Log.e("errornum",cursor.getCount()+"");
                            if (cursor.getCount() == 0) {
                                //添加错词
                                ContentValues word = new ContentValues();
                                word.put("WENGLISH", w.getWenglish());
                                word.put("WCHINESE", w.getWchinese());
                                word.put("WIMGPATH", w.getWimgPath());
                                word.put("UNID", w.getUnid());
                                word.put("BID", w.getBid());
                                word.put("TYPE", w.getType());
                                word.put("ISTRUE", w.getIsTrue());
                                word.put("ADDTIME", simpleDateFormat.format(date));
                                long row = database.insert("TBL_WRONGWORD", null, word);
                                //                    Log.e("插入错词的行号", row + "");
                            }
                            paperlist.get(i).setIsTrue(Constant.SPELL_FALSE);
                        }
                    }
                    //传递测试数据
                    sendScore();
                    AlertDialog.Builder adBuilder = new AlertDialog.Builder(ListenResultSelectActivity.this);
                    adBuilder.setCancelable(false);
                    adBuilder.setTitle("     本次听写成绩：");
                    adBuilder.setMessage("            " + score * 100 + "分");
                    adBuilder.setPositiveButton("返回首页", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ListenResultSelectActivity.this, ListenIndexActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    adBuilder.setNegativeButton("再次听写", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    adBuilder.create().show();
                }
                else{
                    Toast.makeText(ListenResultSelectActivity.this,"请把每个单词都标上√或×哦！",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initView() {
        tvRreturnListenSelect = findViewById(R.id.tv_okListenSelect);
        rvResultPaperSelect = findViewById(R.id.rv_result_paper_select);
        WrongWordDBHelper wrongWordDBHelper = new WrongWordDBHelper(this, "tbl_wrongWord.db", 1);
        database = wrongWordDBHelper.getWritableDatabase();
        CorrectWordDBHelper currectWordDBHelper = new CorrectWordDBHelper(this, "tbl_correctWord.db", 1);
        currectdatabase = currectWordDBHelper.getWritableDatabase();
        CorrectSumDBHelper correctSumDBHelper = new CorrectSumDBHelper(this, "tbl_correctSumWord.db", 1);
        currectsumdatabase = correctSumDBHelper.getWritableDatabase();
        CorrectSumMonthDBHelper correctSumMonthDBHelper = new CorrectSumMonthDBHelper(this, "tbl_correctSumMonthWord.db", 1);
        currectsumMonthdatabase = correctSumMonthDBHelper.getWritableDatabase();
    }

    private void initData() {
        Intent intent = getIntent();
        String str = intent.getStringExtra(Constant.DETAIL_PAPER);
        if (str != null && !str.equals("")) {
            Log.e("paperlist", str);
            Type listType = new TypeToken<List<Word>>() {
            }.getType();
            paperlist = new Gson().fromJson(str, listType);
        }

        checkStatus = new HashMap<>();
        for (int i = 0; i < paperlist.size(); i++) {
            checkStatus.put(i, null);
        }

    }

    private void sendScore() {
        SharedPreferences sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        User user = new Gson().fromJson(sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("sum", sum + "").add("error", error + "").add("right", sum - error + "").add("time", simpleDateFormat.format(date)).add("uid", user.getUid() + "").build();
        Request request = new Request.Builder().url(Constant.URL_SAVE_RECORD).post(fb).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            /**
             * 未完待续
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonBooks = response.body().string();
                Log.e("response", "" + jsonBooks);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
            adBuilder.setTitle("是否退出本次记录");
            adBuilder.setMessage("退出后本次听写将不会生成记录");
            adBuilder.setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 选中“确定”按钮，解除绑定
                    // 更改SharedP中数据
                    finish();
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
