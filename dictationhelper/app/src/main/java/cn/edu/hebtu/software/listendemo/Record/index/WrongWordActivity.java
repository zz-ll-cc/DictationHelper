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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.NewOrWrongTimeWord;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Entity.WrongWord;
import cn.edu.hebtu.software.listendemo.Host.learnWord.LearnWordActivity;
import cn.edu.hebtu.software.listendemo.Host.listenWord.ListenWordActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import cn.edu.hebtu.software.listendemo.Untils.WrongWordDBHelper;

public class WrongWordActivity extends AppCompatActivity {
    public static final String DELETE_FROM = "DELETE_FROM";
    public static final String DELETE_FROM_WRONG_WORD = "WRONG_WORD";
    public static final String DELETE_ITEM_TIME = "ITEM_TIME";
    private RelativeLayout rlNewEmpty;
    private ImageView ivEmpty;
    private LinearLayout llHave;
    private TextView tvNewTiltle;
    private Button btnStudy;
    private Button btnListen;
    private WrongWordRecyclerTimeAdapter adapter;
    private RecyclerView recyclerViewNewOrWrong;
    private List<Word> wordkList = new ArrayList<>();
    private List<NewOrWrongTimeWord> wrongWordList = new ArrayList<>();
    private ImageView ivExit;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_neworwrongword);
        StatusBarUtil.statusBarLightMode(this);
        EventBus.getDefault().register(this);
        initData();
        initView();
        tvNewTiltle.setText("错词本");
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
                    Toast.makeText(WrongWordActivity.this, "先去听写，祝你永无错词", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(WrongWordActivity.this, LearnWordActivity.class);
                    intent.putExtra(Constant.WRONGWORD_CON_LEARNWORD_LEARN, new Gson().toJson(wordkList));
                    startActivity(intent);
                }
            }
        });
        btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == wordkList || wordkList.isEmpty()) {
                    Toast.makeText(WrongWordActivity.this, "先去听写，祝你永无错词", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(WrongWordActivity.this, ListenWordActivity.class);
                    intent.putExtra(Constant.WRONGWORD_CON_LEARNWORD_DICTATION, new Gson().toJson(wordkList));
                    startActivity(intent);
                }
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteNewWord(Map<String, Object> deleteMap) {
        Log.e("wrong_word",DELETE_FROM_WRONG_WORD);
        String deleteType = deleteMap.get(DELETE_FROM).toString();
        if (deleteType.equals(DELETE_FROM_WRONG_WORD)) {
            adapter.deleteItem(deleteMap.get(DELETE_ITEM_TIME).toString());
        }
    }
    private void initView() {
        tvNewTiltle = findViewById(R.id.tv_neworwrong);
        btnStudy = findViewById(R.id.btn_study);
        ivExit = findViewById(R.id.iv_record_now_exit);
        btnListen = findViewById(R.id.btn_listen);
        rlNewEmpty = findViewById(R.id.rl_empty_record);
        ivEmpty = findViewById(R.id.iv_empty);
        llHave = findViewById(R.id.ll_have_record);
        recyclerViewNewOrWrong = findViewById(R.id.rc_neworwrong);

        if (wordkList.isEmpty()){
            rlNewEmpty.setVisibility(View.VISIBLE);
            ivEmpty.setImageResource(R.drawable.empty_wrong);
            rlNewEmpty.setBackgroundColor(Color.parseColor("#FFFFFF"));
            llHave.setVisibility(View.GONE);
        }else{
            rlNewEmpty.setVisibility(View.GONE);
            llHave.setVisibility(View.VISIBLE);
        }

        adapter = new WrongWordRecyclerTimeAdapter(wrongWordList,this, R.layout.activity_record_neworwrongword_out_item,ivEmpty,rlNewEmpty,llHave);
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
        WrongWordDBHelper wrongWordDBHelper = new WrongWordDBHelper(this, "tbl_wrongWord.db", 1);
        SQLiteDatabase database = wrongWordDBHelper.getWritableDatabase();
        Cursor cursor = database.query("TBL_WRONGWORD", null, null, null, null, null, "ADDTIME desc");
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
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(addTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                addTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
                Word word = new Word();
                word.setWenglish(wenglish);
                word.setWchinese(wchinese);
                word.setWimgPath(wimgPath);
                word.setUnid(unid);
                word.setBid(bid);
                word.setType(type);
                word.setIsTrue(isTrue);

                wordkList.add(word);
                addWordIntoNewWordList(word,addTime);
            } while (cursor.moveToNext());
        }
    }
    private void addWordIntoNewWordList(Word word, String addTime) {
        if (wrongWordList.size() == 0){ // 此时一个数据都没有
            List<Word> words = new ArrayList<>();
            words.add(word);
            NewOrWrongTimeWord timeWord = new NewOrWrongTimeWord();
            timeWord.setTime(addTime);
            timeWord.setWords(words);
            wrongWordList.add(timeWord);
        }else{  // 此时newWordList不为空
            boolean haveWords = false;
            for (int i = 0;i<wrongWordList.size();i++){
                NewOrWrongTimeWord timeWord = wrongWordList.get(i);
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
                wrongWordList.add(timeWord);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
