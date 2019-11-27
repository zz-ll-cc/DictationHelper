package com.example.dictationprj.BookDetail;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dictationprj.Book;
import com.example.dictationprj.R;
import com.example.dictationprj.Unit;
import com.example.dictationprj.Word;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private ImageView ivExit;
    private TextView tvName;
    private ImageView ivCollect;
    private ImageView ivBind;
    private ImageView ivCover;
    private CheckBox cbChooseAll;
    private RecyclerView rvBookDetail;
    private LinearLayout llRecite;
    private LinearLayout llDictation;
    private Book book;
    private List<Unit> units = new ArrayList<>();
    private SharedPreferences sp;
    private UnitRecyclerAdapter adapter;
    private Gson gson = new Gson();
    private boolean isBind = false;
    private boolean isCollected = false;
    private List<Integer> colList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        initView();
        initData();
        setListener();
    }

    private void setListener() {
        adapter.setCbChooseAllListener();
        adapter.setStartLearnOrDictateListener();
        BookDetailListener listener = new BookDetailListener();
        ivExit.setOnClickListener(listener);
        ivBind.setOnClickListener(listener);
        ivCollect.setOnClickListener(listener);
    }

    private class BookDetailListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_book_detail_exit:
                    finish();
                    break;
                case R.id.iv_book_detail_bind:
                    if (isBind){
                        // 显示一个Dialog
                        AlertDialog.Builder adBuilder = new AlertDialog.Builder(BookDetailActivity.this);
                        adBuilder.setTitle("确定解除绑定");

                        adBuilder.setPositiveButton("确认解除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 选中“确定”按钮，解除绑定
                                // 更改SharedP中数据
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("bind", -1);
                                // 修改显示样式
                                ivBind.setImageDrawable(getResources().getDrawable(R.drawable.bind_no));
                                ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collect_no));
                                // 修改收藏列表
                                for (int i=0;i<colList.size();i++){
                                    if (colList.get(i) == book.getId()){
                                        colList.remove(i);
                                        break;
                                    }
                                }
                                editor.putString("collectList",gson.toJson(colList));
                                editor.commit();
                                Toast.makeText(BookDetailActivity.this, "已解除绑定教材", Toast.LENGTH_SHORT).show();
                                isBind = false;
                            }
                        });
                        adBuilder.setNegativeButton("取消解除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 选中“取消”按钮，取消界面

                            }
                        });

                        adBuilder.create().show();
                    }
                    else{
                        // 显示一个Dialog
                        AlertDialog.Builder adBuilder = new AlertDialog.Builder(BookDetailActivity.this);
                        adBuilder.setTitle("确定绑定");

                        adBuilder.setPositiveButton("确认绑定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 选中“确定”按钮，解除绑定
                                // 更改SharedP中数据
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("bind", book.getId());

                                // 修改显示样式
                                ivBind.setImageDrawable(getResources().getDrawable(R.drawable.binded));
                                ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collected));
                                // 修改收藏列表
                                colList.add(book.getId());
                                editor.putString("collectList",gson.toJson(colList));
                                editor.commit();
                                Toast.makeText(BookDetailActivity.this, "成功绑定教材，默认收藏", Toast.LENGTH_SHORT).show();
                                isBind = true;
                            }
                        });
                        adBuilder.setNegativeButton("取消绑定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 选中“取消”按钮，取消界面

                            }
                        });

                        adBuilder.create().show();
                    }
                    break;
                case R.id.iv_book_detail_collect:
                    if (isCollected && isBind){
                        // 如果当前为已收藏教材，且为已绑定
                        Log.e("eassa","else: bind:"+isBind+" coll:"+isCollected);
                        Toast.makeText(BookDetailActivity.this,"教材已绑定，默认收藏，无需取消",Toast.LENGTH_SHORT).show();
                    }else if (isCollected && !isBind){
                        // 当前为已收藏，未绑定
                        Log.e("eassa","else: bind:"+isBind+" coll:"+isCollected);
                        SharedPreferences.Editor editor = sp.edit();
                        for (int i=0;i<colList.size();i++){
                            if (colList.get(i) == book.getId()){
                                colList.remove(i);
                                break;
                            }
                        }
                        editor.putString("collectList",gson.toJson(colList));
                        editor.commit();
                        isCollected = false;
                        ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collect_no));
                    }else{
                        // 当前为未收藏
                        Log.e("eassa","else: bind:"+isBind+" coll:"+isCollected);
                        SharedPreferences.Editor editor = sp.edit();
                        colList.add(book.getId());
                        editor.putString("collectList",gson.toJson(colList));
                        editor.commit();
                        isCollected = true;
                        ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collected));
                    }
                    break;
            }
        }
    }
    private void initData() {
        book = (Book) getIntent().getSerializableExtra("book");
        tvName.setText(book.getName());
        sp = getSharedPreferences("用户", MODE_PRIVATE);
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        colList = gson.fromJson(sp.getString("collectList", "[]"), type);
        if (colList.contains(book.getId()))
            isCollected = true;
        int bindId = sp.getInt("bind", -1);
        if (bindId == book.getId())
            isBind = true;

        List<Word> words1 = new ArrayList<>();
        Word word1 = new Word();
        word1.setId(1);
        word1.setChinese("苹果");
        word1.setEnglish("apple");
        Word word2 = new Word();
        word2.setId(2);
        word2.setChinese("香蕉");
        word2.setEnglish("banana");
        words1.add(word1);
        words1.add(word2);

        List<Word> words2 = new ArrayList<>();
        Word word3 = new Word();
        word3.setId(3);
        word3.setChinese("梨");
        word3.setEnglish("pare");
        Word word4 = new Word();
        word4.setId(4);
        word4.setChinese("水果");
        word4.setEnglish("fruit");
        words2.add(word3);
        words2.add(word4);

        Unit unit1 = new Unit();
        unit1.setWords(words1);
        unit1.setId(0);
        unit1.setUnitName("Unit1");
        unit1.setUnitTitle("Serialize");

        Unit unit2 = new Unit();
        unit2.setId(1);
        unit2.setWords(words2);
        unit2.setUnitName("Unit2");
        unit2.setUnitTitle("THis");

        units.add(unit1);
        units.add(unit2);
        adapter = new UnitRecyclerAdapter(this, R.layout.fragment_book_detail_item, units, cbChooseAll, llRecite, llDictation);
        rvBookDetail.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);   // 默认设置垂直布局
        rvBookDetail.setLayoutManager(layoutManager);

        if (isBind)
            ivBind.setImageDrawable(getResources().getDrawable(R.drawable.binded));
        else
            ivBind.setImageDrawable(getResources().getDrawable(R.drawable.bind_no));

        if (isCollected)
            ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collected));
        else if(!isCollected && isBind)
            ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collected));
        else
            ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collect_no));
    }

    private void initView() {
        ivExit = findViewById(R.id.iv_book_detail_exit);
        tvName = findViewById(R.id.tv_book_detail_book_name);
        ivCollect = findViewById(R.id.iv_book_detail_collect);
        ivBind = findViewById(R.id.iv_book_detail_bind);
        ivCover = findViewById(R.id.iv_book_detail_cover);
        cbChooseAll = findViewById(R.id.cb_book_detail_chooseAll);
        rvBookDetail = findViewById(R.id.recv_book_detail);
        llRecite = findViewById(R.id.ll_book_detail_recite);
        llDictation = findViewById(R.id.ll_book_detail_dictation);
    }
}
