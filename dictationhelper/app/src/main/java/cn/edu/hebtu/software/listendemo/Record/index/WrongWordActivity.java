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

import com.google.gson.Gson;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.WrongWord;
import cn.edu.hebtu.software.listendemo.Host.learnWord.LearnWordActivity;
import cn.edu.hebtu.software.listendemo.Host.listenWord.ListenWordActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.WrongWordDBHelper;

public class WrongWordActivity extends AppCompatActivity {
    private TextView tvNewTiltle;
    private Button btnStudy;
    private Button btnListen;
    private WrongWordRecyclerViewAdapter wrongWordRecyclerViewAdapter;
    private RecyclerView recyclerViewNewOrWrong;
    private List<WrongWord> wordkList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_neworwrongword);
        initData();
        initView();
        tvNewTiltle.setText("错词本");
        btnStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WrongWordActivity.this, LearnWordActivity.class);
                intent.putExtra(Constant.WRONGWORD_CON_LEARNWORD_LEARN,new Gson().toJson(wordkList));
                startActivity(intent);
            }
        });
        btnListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WrongWordActivity.this, ListenWordActivity.class);
                intent.putExtra(Constant.WRONGWORD_CON_LEARNWORD_DICTATION,new Gson().toJson(wordkList));
                startActivity(intent);
            }
        });
    }

    private void initView() {
        tvNewTiltle = findViewById(R.id.tv_neworwrong);
        btnStudy = findViewById(R.id.btn_study);
        btnListen = findViewById(R.id.btn_listen);
        recyclerViewNewOrWrong = findViewById(R.id.rc_neworwrong);
        wrongWordRecyclerViewAdapter = new WrongWordRecyclerViewAdapter(this, wordkList, R.layout.activity_record_neworwrongword_item);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewNewOrWrong.setLayoutManager(layoutManager);
        recyclerViewNewOrWrong.setAdapter(wrongWordRecyclerViewAdapter);
    }

    private void initData() {
        String path = getFilesDir().getPath() + "/database";
        Log.e("项目内部文件的根目录：", path);
        File file = new File(path);
        //创建目录
        if (!file.exists()) {
            file.mkdirs();//创建路径
        }
        WrongWordDBHelper wrongWordDBHelper =new WrongWordDBHelper(this,"tbl_wrongWord.db",1);
        SQLiteDatabase database = wrongWordDBHelper.getWritableDatabase();
        Cursor cursor =database .query("TBL_WRONGWORD", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            wordkList = new ArrayList<>();
            do {
                String wenglish = cursor.getString(cursor.getColumnIndex("WENGLISH"));
                String wchinese = cursor.getString(cursor.getColumnIndex("WCHINESE"));
                String wimgPath= cursor.getString(cursor.getColumnIndex("WIMGPATH"));
                int unid = cursor.getInt(cursor.getColumnIndex("UNID"));
                int bid = cursor.getInt(cursor.getColumnIndex("BID"));
                int type= cursor.getInt(cursor.getColumnIndex("TYPE"));
                int isTrue= cursor.getInt(cursor.getColumnIndex("ISTRUE"));
                String addTime= cursor.getString(cursor.getColumnIndex("ADDTIME"));
                WrongWord word = new WrongWord();
                word.setWenglish(wenglish);
                word.setWchinese(wchinese);
                word.setWimgPath(wimgPath);
                word.setUnid(unid);
                word.setBid(bid);
                word.setType(type);
                word.setIsTrue(isTrue);
                SimpleDateFormat formatter = new SimpleDateFormat( "yyyy年MM月dd日 HH:mm:ss");
                Date date = null;
                try {
                    date = formatter.parse(addTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                word.setTime(date);
                wordkList.add(word);
            } while (cursor.moveToNext());
            Log.e("错词本大小", wordkList.size() + "");
        }
    }


}