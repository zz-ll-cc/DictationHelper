package cn.edu.hebtu.software.listendemo.Record.index;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.NewOrWrongTimeWord;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.learnWord.LearnWordActivity;
import cn.edu.hebtu.software.listendemo.Host.listenWord.ListenWordActivity;
import cn.edu.hebtu.software.listendemo.Mine.index.credit.SpacesItemDecoration;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.NewWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;

public class NewWordActivity extends AppCompatActivity {
    public static final String DELETE_FROM = "DELETE_FROM";
    public static final String DELETE_FROM_NEW_WORD = "NEW_WORD";
    public static final String DELETE_ITEM_TIME = "ITEM_TIME";
    private RelativeLayout rlNewEmpty;
    private LinearLayout llHave;
    private ImageView ivEmpty;
    private TextView tvNewTiltle;
    private Button btnStudy;
    private Button btnListen;
    private NewWordRecyclerTimeAdapter adapter;
    private RecyclerView recyclerViewNewOrWrong;
    private List<Word> wordkList = new ArrayList<>();
    private List<NewOrWrongTimeWord> newWordList = new ArrayList<>();
    private ImageView ivExit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_neworwrongword);
        StatusBarUtil.statusBarLightMode(this);
        EventBus.getDefault().register(this);
        initData();
        initView();
        tvNewTiltle.setText("生词本");
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == wordkList || wordkList.isEmpty()) {
                    Toast.makeText(NewWordActivity.this, "快去学习然后添加生词吧！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(NewWordActivity.this, LearnWordActivity.class);
                    intent.putExtra(Constant.NEWWORD_CON_LEARNWORD_LEARN, new Gson().toJson(wordkList));
                    startActivity(intent);
                }
            }
        });
        btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == wordkList || wordkList.isEmpty()) {
                    Toast.makeText(NewWordActivity.this, "快去学习然后添加生词吧！", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(NewWordActivity.this, ListenWordActivity.class);
                    intent.putExtra(Constant.NEWWORD_CON_LEARNWORD_DICTATION, new Gson().toJson(wordkList));
                    startActivity(intent);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteNewWord(Map<String, Object> deleteMap) {
        String deleteType = deleteMap.get(DELETE_FROM).toString();
        if (deleteType.equals(DELETE_FROM_NEW_WORD)) {
            adapter.deleteItem(deleteMap.get(DELETE_ITEM_TIME).toString());
        }
    }

    private void initView() {
        tvNewTiltle = findViewById(R.id.tv_neworwrong);
        btnStudy = findViewById(R.id.btn_study);
        btnListen = findViewById(R.id.btn_listen);
        ivExit = findViewById(R.id.iv_record_now_exit);
        rlNewEmpty = findViewById(R.id.rl_empty_record);
        ivEmpty = findViewById(R.id.iv_empty);
        llHave = findViewById(R.id.ll_have_record);
        recyclerViewNewOrWrong = findViewById(R.id.rc_neworwrong);
        if (wordkList.isEmpty()) {
            rlNewEmpty.setVisibility(View.VISIBLE);
            ivEmpty.setImageResource(R.drawable.empty_new);
            rlNewEmpty.setBackgroundColor(Color.parseColor("#C6DBDE"));
            llHave.setVisibility(View.GONE);
        } else {
            rlNewEmpty.setVisibility(View.GONE);
            llHave.setVisibility(View.VISIBLE);
        }
        adapter = new NewWordRecyclerTimeAdapter(newWordList,this, R.layout.activity_record_neworwrongword_out_item, ivEmpty, rlNewEmpty, llHave);

        recyclerViewNewOrWrong.setAdapter(adapter);

    }

    private void initData() {
        String path = getFilesDir().getPath() + "/database";
        Log.e("项目内部文件的根目录：", path);
        File file = new File(path);
        //创建目录
        if (!file.exists()) {
            file.mkdirs();//创建路径
        }
        NewWordDBHelper newWordDBHelper = new NewWordDBHelper(this, "tbl_newWord.db", 1);
        SQLiteDatabase database = newWordDBHelper.getWritableDatabase();
        Cursor cursor = database.query("TBL_NEWWORD", null, null, null, null, null, "ADDTIME desc");
        if (cursor.moveToFirst()) {
            wordkList = new ArrayList<>();
            do {
                String wenglish = cursor.getString(cursor.getColumnIndex("WENGLISH"));
                String wchinese = cursor.getString(cursor.getColumnIndex("WCHINESE"));
                String wimgPath = cursor.getString(cursor.getColumnIndex("WIMGPATH"));
                int unid = cursor.getInt(cursor.getColumnIndex("UNID"));
                int bid = cursor.getInt(cursor.getColumnIndex("BID"));
                int type = cursor.getInt(cursor.getColumnIndex("TYPE"));
                int isTrue = cursor.getInt(cursor.getColumnIndex("ISTRUE"));
                String addTime = cursor.getString(cursor.getColumnIndex("ADDTIME"));
                Word word = new Word();
                word.setWenglish(wenglish);
                word.setWchinese(wchinese);
                word.setWimgPath(wimgPath);
                word.setUnid(unid);
                word.setBid(bid);
                word.setType(type);
                word.setIsTrue(isTrue);
                // 添加至全部的单词列表中
                wordkList.add(word);
                // 添加至newWordList
                addWordIntoNewWordList(word,addTime);

            } while (cursor.moveToNext());
//            Log.e("生词本大小", wordkList.size() + "");
        }
    }

    private void addWordIntoNewWordList(Word word,String addTime) {
        if (newWordList.size() == 0){ // 此时一个数据都没有
            List<Word> words = new ArrayList<>();
            words.add(word);
            NewOrWrongTimeWord timeWord = new NewOrWrongTimeWord();
            timeWord.setTime(addTime);
            timeWord.setWords(words);
            newWordList.add(timeWord);
        }else{  // 此时newWordList不为空
            boolean haveWords = false;
            for (int i = 0;i<newWordList.size();i++){
                NewOrWrongTimeWord timeWord = newWordList.get(i);
                if (timeWord.getTime().equals(addTime)){
                    timeWord.getWords().add(word);
                    haveWords = true;
                    break;
                }
            }
            if (!haveWords){
                List<Word> words = new ArrayList<>();
                words.add(word);
                NewOrWrongTimeWord timeWord = new NewOrWrongTimeWord();
                timeWord.setTime(addTime);
                timeWord.setWords(words);
                newWordList.add(timeWord);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
