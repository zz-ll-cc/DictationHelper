package cn.edu.hebtu.software.listendemo.Host.learnWord;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Entity.WrongWord;
import cn.edu.hebtu.software.listendemo.Host.listenWord.ListenWordActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.LearnWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.NewWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;

import static cn.edu.hebtu.software.listendemo.Untils.Constant.LEARN_DB_NAME;
import static cn.edu.hebtu.software.listendemo.Untils.LearnWordDBHelper.TABLE_LEARN;

public class LearnWordActivity extends AppCompatActivity {

    private LearnWordRecyclerViewAdapter learnWordRecyclerViewAdapter;
    private RecyclerView recyclerViewLearnWord;
    private List<Word> learnWordlist;
    private List<WrongWord> errorWordlist;
    private int i = 1;
    private PopupWindow popupWindow = null;
    private View popupView = null;
    private SQLiteDatabase database;

    private LearnWordDBHelper learnWordDBHelper;
    private SQLiteDatabase learnDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_word);
        setTitle("学习单词");
        NewWordDBHelper newWordDBHelper = new NewWordDBHelper(this, "tbl_newWord.db", 1);
        database = newWordDBHelper.getWritableDatabase();
        learnWordDBHelper = new LearnWordDBHelper(this, LEARN_DB_NAME, 1);
        learnDB = learnWordDBHelper.getWritableDatabase();
        initData();
        initView();
        StatusBarUtil.statusBarLightMode(this);
    }

    private void initView() {
        recyclerViewLearnWord = findViewById(R.id.rv_learnword);
        learnWordRecyclerViewAdapter = new LearnWordRecyclerViewAdapter(this, learnWordlist, R.layout.activity_learnword_recycler_item, database);
        recyclerViewLearnWord.setAdapter(learnWordRecyclerViewAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerViewLearnWord.setLayoutManager(linearLayoutManager);
        final Button btnNext = findViewById(R.id.btn_next);
        Button btnPrevious = findViewById(R.id.btn_previous);
        final RecyclerView.LayoutManager layoutManager = recyclerViewLearnWord.getLayoutManager();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i != learnWordlist.size()) {
                    i += 1;
                }
                if (i == 2) {
                    btnPrevious.setBackground(getResources().getDrawable(R.drawable.btn_selector));
                    btnPrevious.setTextColor(getResources().getColor(R.color.black));
                }

                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    int postion = firstItemPosition + 1;
                    addLearnWord(postion);
                    if (firstItemPosition < learnWordlist.size() - 1) {
                        recyclerViewLearnWord.scrollToPosition(firstItemPosition + 1);
                    }
                    if (postion == learnWordlist.size() - 1) {
                        btnNext.setText("去听写");
                    }
                    if (firstItemPosition == learnWordlist.size() - 1) {
                        //创建并显示自定义的dialog
                        CustomDialogLearnWord dialog = new CustomDialogLearnWord(learnWordlist, LearnWordActivity.this);
                        dialog.setCancelable(false);
                        dialog.show(getSupportFragmentManager(), "learn");

                        //showPopupView(v);
                    }
                }
            }
        });

        btnPrevious.setBackground(getResources().getDrawable(R.drawable.btn_invalid));
        btnPrevious.setTextColor(getResources().getColor(R.color.bar_grey));

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i != 1) {
                    i -= 1;
                }
                if (i == 1) {
                    btnPrevious.setBackground(getResources().getDrawable(R.drawable.btn_invalid));
                    btnPrevious.setTextColor(getResources().getColor(R.color.bar_grey));
                }
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    Log.e("firstItemPosition", firstItemPosition + "");
                    if (firstItemPosition > 0) {
                        recyclerViewLearnWord.scrollToPosition(firstItemPosition - 1);
                        btnNext.setText("上一个");
                    } else {
                        Toast.makeText(LearnWordActivity.this, "已经是第一个了", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
    private void addLearnWord(int position){
        Word word = learnWordlist.get(position-1);
        Log.e("wordNow",word.toString());
        Cursor cursor = learnDB.query(TABLE_LEARN,null,"WORDID = ?",new String[]{word.getWid()+""},null,null,null);
        if (cursor.getCount()==0){
            ContentValues cv = new ContentValues();
            cv.put("BOOKID",word.getBid());
            cv.put("WORDID",word.getWid());
            learnDB.insert(TABLE_LEARN,null,cv);
        }

    }
    private void initData() {
        Intent intent = getIntent();
        String str = intent.getStringExtra(Constant.DETAIL_CON_RECITE_OR_DICTATION);
        if (str != null) {
            Type listType = new TypeToken<List<Word>>() {
            }.getType();
            learnWordlist = new Gson().fromJson(str, listType);
        }
        String str1 = intent.getStringExtra(Constant.NEWWORD_CON_LEARNWORD_LEARN);
        if (str1 != null) {
            Type listType = new TypeToken<List<Word>>() {
            }.getType();
            learnWordlist = new Gson().fromJson(str1, listType);
            str1 = null;
        }
        String str2 = intent.getStringExtra(Constant.WRONGWORD_CON_LEARNWORD_LEARN);
        if (str2 != null) {
            Type listType = new TypeToken<List<WrongWord>>() {
            }.getType();
            errorWordlist = new Gson().fromJson(str2, listType);
            learnWordlist = new ArrayList<>();
            for (WrongWord w : errorWordlist) {
                Word word = new Word();
                word.setWenglish(w.getWenglish());
                word.setWchinese(w.getWchinese());
                word.setWimgPath(w.getWimgPath());
                word.setBid(w.getBid());
                word.setIsTrue(w.getIsTrue());
                word.setType(w.getType());
                word.setUnid(w.getUnid());
                learnWordlist.add(word);
            }
            str2 = null;
        }
    }

    private void showPopupView(View v) {
        popupView = LayoutInflater.from(this).inflate(R.layout.custom_dialog_finishlearn, null);
        popupWindow = new PopupWindow(popupView, dip2px(this, 300), dip2px(this, 200), true);
        //设置PopupWindow显示内容视图
        //popupWindow.setContentView(popupView);
        //设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(false);
        //设置PopupWindow是否能响内部点击事件
        popupWindow.setTouchable(true);
        Button btnOk = popupView.findViewById(R.id.btn_ok_learn);
        Button btnCancel = popupView.findViewById(R.id.btn_cancel_learn);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                Intent intent = new Intent(LearnWordActivity.this, ListenWordActivity.class);
                intent.putExtra(Constant.RECITE_CON_DICTATION, gson.toJson(learnWordlist));
                startActivity(intent);
                finish();
                popupWindow.dismiss();
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


