package cn.edu.hebtu.software.listendemo.Record.index;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.learnWord.LearnWordActivity;
import cn.edu.hebtu.software.listendemo.Host.listenWord.ListenWordActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.NewWordDBHelper;

public class NewWordActivity extends AppCompatActivity {
    private TextView tvNewTiltle;
    private Button btnStudy;
    private Button btnListen;
    private NewWordRecyclerViewAdapter newWordRecyclerViewAdapter;
    private RecyclerView recyclerViewNewOrWrong;
    private List<Word> wordkList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_neworwrongword);
        initData();
        initView();
        tvNewTiltle.setText("生词本");
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

    private void initView() {
        tvNewTiltle = findViewById(R.id.tv_neworwrong);
        btnStudy = findViewById(R.id.btn_study);
        btnListen = findViewById(R.id.btn_listen);
        recyclerViewNewOrWrong = findViewById(R.id.rc_neworwrong);
        newWordRecyclerViewAdapter = new NewWordRecyclerViewAdapter(this, wordkList, R.layout.activity_record_neworwrongword_item);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewNewOrWrong.setLayoutManager(layoutManager);
        recyclerViewNewOrWrong.setAdapter(newWordRecyclerViewAdapter);
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
        Cursor cursor = database.query("TBL_NEWWORD", null, null, null, null, null, null);
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
                Word word = new Word();
                word.setWenglish(wenglish);
                word.setWchinese(wchinese);
                word.setWimgPath(wimgPath);
                word.setUnid(unid);
                word.setBid(bid);
                word.setType(type);
                word.setIsTrue(isTrue);
                wordkList.add(word);
            } while (cursor.moveToNext());
            Log.e("生词本大小", wordkList.size() + "");
        }
    }

}
