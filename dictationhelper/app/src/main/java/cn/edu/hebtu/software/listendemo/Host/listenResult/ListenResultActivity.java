package cn.edu.hebtu.software.listendemo.Host.listenResult;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.index.ListenIndexActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.CorrectSumDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.CorrectWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import cn.edu.hebtu.software.listendemo.Untils.WrongWordDBHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListenResultActivity extends AppCompatActivity {
    private ListenResultRecyclerViewAdapter adapter;
    private RecyclerView rvResult;
    private TextView tvReturnHost;
    private TextView tvReturnBookDetail;
    private List<Word> successList;
    private List<Word> mineList;
    private SQLiteDatabase database;

    private SQLiteDatabase currectdatabase;
    private SQLiteDatabase currectsumdatabase;
    private  int sum;
    private int error=0;
    private int correct=0;
    private  double score;
    public Date date;
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
    public SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_result);
        StatusBarUtil.setStatusBarColor(this,R.color.bar_result);
        WrongWordDBHelper wrongWordDBHelper =new WrongWordDBHelper(this,"tbl_wrongWord.db",1);
        database =wrongWordDBHelper.getWritableDatabase();
        CorrectWordDBHelper currectWordDBHelper =new CorrectWordDBHelper(this,"tbl_correctWord.db",1);
        currectdatabase =currectWordDBHelper.getWritableDatabase();
        CorrectSumDBHelper correctSumDBHelper =new CorrectSumDBHelper(this,"tbl_correctSumWord.db",1);
        currectsumdatabase =correctSumDBHelper.getWritableDatabase();
        initData();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StatusBarUtil.setStatusBarColor(this,R.color.bar_grey);
    }

    private void initView() {
        rvResult = findViewById(R.id.rv_result);
        tvReturnHost = findViewById(R.id.tv_returnHost);
        tvReturnBookDetail = findViewById(R.id.tv_returnBookDetail);
        //返回到主页
        sum = successList.size();
        date = new Date(System.currentTimeMillis());
        Log.e("date", "" + date.toString());
        for (int i = 0; i < successList.size(); i++) {
            successList.get(i).setIsTrue(Constant.SPELL_TRUE);
            if (mineList.get(i).getWenglish().equals(successList.get(i).getWenglish())) {
                mineList.get(i).setIsTrue(Constant.SPELL_TRUE);
                Word w1=successList.get(i);
                Cursor cursor1 =currectdatabase.query("TBL_CURRECTWORD", null, "WENGLISH=?", new String[]{w1.getWenglish()}, null, null, null);
                Log.e("currectnum",cursor1.getCount()+"");
                if(cursor1.getCount()==0) {
                    //添加错词
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
                    Log.e("插入正确词的行号", row + "");
                    correct++;
                }
                Cursor cursor2 =currectsumdatabase.query("TBL_CURRECTSUM", null, "ADDTIME=?",new String[]{simpleDateFormat1.format(date)}, null, null, null);
                Log.e("currectsumnum",cursor1.getCount()+"");
                if(cursor2.getCount()==0) {
                    //添加错词
                    ContentValues word = new ContentValues();
                    word.put("SUM", correct);
                    word.put("ADDTIME", simpleDateFormat1.format(date));
                    long row = currectsumdatabase.insert("TBL_CURRECTSUM", null, word);
                    Log.e("插入正确词总数的行号", row + "");
                    correct++;
                }
                if(cursor2.getCount()==1){
                    if(cursor2.moveToFirst()){
                        int sum=cursor2.getInt(cursor2.getColumnIndex("SUM"));
                        ContentValues word = new ContentValues();
                        word.put("SUM", correct+sum);
                        word.put("ADDTIME", simpleDateFormat1.format(date));
                        currectsumdatabase.update("TBL_CURRECTSUM",word,"ADDTIME=?",new String[]{simpleDateFormat1.format(date)});
                    }while(cursor2.moveToNext());
                }

            }else{
                Word w=successList.get(i);
                Cursor cursor =database .query("TBL_WRONGWORD", null, "WENGLISH=?", new String[]{w.getWenglish()}, null, null, null);
                Log.e("errornum",cursor.getCount()+"");
                if(cursor.getCount()==0) {
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
                    Log.e("插入错词的行号", row + "");
                }
                mineList.get(i).setIsTrue(Constant.SPELL_FALSE);
                error++;
            }
        }
        //传递测试数据
        sendScore();
        final LeanTextView mText = findViewById(R.id.lean);
        score = (sum - error) / (sum * 1.0);
        mText.setText(Html.fromHtml("<u>" + Math.round(score * 100) + "</u>"));
        Log.e("sum", "" + sum + "erro:" + error);
        mText.setmDegrees(20);
        //设置适配器
        adapter = new ListenResultRecyclerViewAdapter(this, successList, mineList, R.layout.activity_listen_result_word_item);
        //必须调用，设置布局管理器
        rvResult.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvResult.setLayoutManager(layoutManager);
        tvReturnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListenResultActivity.this, ListenIndexActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvReturnBookDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    private void initData() {
        Intent intent = getIntent();
        String str = intent.getStringExtra("success");
        String str1 = intent.getStringExtra("mine");
        Type listType = new TypeToken<List<Word>>() {
        }.getType();
        if (str != null && !str.equals("")) {
            successList = new Gson().fromJson(str, listType);
        }
        if (str1 != null && !str1.equals("")) {
            mineList = new Gson().fromJson(str1, listType);
        }
    }

}
