package cn.edu.hebtu.software.listendemo.Host.bookDetail;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Book;
import cn.edu.hebtu.software.listendemo.Entity.Unit;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookDetailActivity extends AppCompatActivity {
    private int nowCount;
    private int requestCount;
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
    private List<Integer> colList;
    private OkHttpClient client = new OkHttpClient();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == requestCount){
                initView();
                setListener();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        findView();
        initData();

    }

    private void setListener() {
        adapter.setCbChooseAllListener();
        adapter.setStartLearnOrDictateListener();
        BookDetailListener listener = new BookDetailListener();
        ivExit.setOnClickListener(listener);
        ivBind.setOnClickListener(listener);
        ivCollect.setOnClickListener(listener);
    }

    private class BookDetailListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_book_detail_exit:
                    finish();
                    break;
                case R.id.iv_book_detail_bind:
                    if (isBind) {
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
                                for (int i = 0; i < colList.size(); i++) {
                                    if (colList.get(i) == book.getBid()) {
                                        colList.remove(i);
                                        break;
                                    }
                                }
                                editor.putString("collectList", gson.toJson(colList));
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
                    } else {
                        // 显示一个Dialog
                        AlertDialog.Builder adBuilder = new AlertDialog.Builder(BookDetailActivity.this);
                        adBuilder.setTitle("确定绑定");

                        adBuilder.setPositiveButton("确认绑定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 选中“确定”按钮，解除绑定
                                // 更改SharedP中数据
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("bind", book.getBid());

                                // 修改显示样式
                                ivBind.setImageDrawable(getResources().getDrawable(R.drawable.binded));
                                ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collected));
                                // 修改收藏列表
                                colList.add(book.getBid());
                                editor.putString("collectList", gson.toJson(colList));
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
                    if (isCollected && isBind) {
                        // 如果当前为已收藏教材，且为已绑定
                        Toast.makeText(BookDetailActivity.this, "教材已绑定，默认收藏，无需取消", Toast.LENGTH_SHORT).show();
                    } else if (isCollected && !isBind) {
                        // 当前为已收藏，未绑定
                        SharedPreferences.Editor editor = sp.edit();
                        for (int i = 0; i < colList.size(); i++) {
                            if (colList.get(i) == book.getBid()) {
                                colList.remove(i);
                                break;
                            }
                        }
                        editor.putString("collectList", gson.toJson(colList));
                        editor.commit();
                        isCollected = false;
                        ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collect_no));
                    } else {
                        // 当前为未收藏
                        Log.e("eassa", "else: bind:" + isBind + " coll:" + isCollected);
                        SharedPreferences.Editor editor = sp.edit();
                        colList.add(book.getBid());
                        editor.putString("collectList", gson.toJson(colList));
                        editor.commit();
                        isCollected = true;
                        ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collected));
                    }
                    break;
            }
        }
    }

    private void initData() {
        book = (Book) getIntent().getSerializableExtra(Constant.HOST_CON_DETAIL_BOOK);
        tvName.setText(book.getBname());
        sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        colList = gson.fromJson(sp.getString(Constant.COLLECT_KEY, Constant.DEFAULT_COLLECT_LIST), type);
        if (colList.contains(book.getBid()))
            isCollected = true;
        int bindId = sp.getInt(Constant.BIND_KEY, Constant.DEFAULT_BIND_ID);
        if (bindId == book.getBid())
            isBind = true;
        // 1. 根据单元数 创建这本书的单元List
        for (int i = 0; i < book.getBunitAccount(); i++) {
            Unit unit = new Unit();
            unit.setUnid(10000 + i);
            unit.setBid(book.getBid());
            unit.setUnName("UNIT "+(i+1));
            units.add(unit);
        }
        int i = 0;
        for(final Unit unit:units){
            // 根据单元，查单词
            FormBody fb = new FormBody.Builder().add("bid",book.getBid()+"").add("unid",unit.getUnid()+"").build();
            Request request = new Request.Builder().url(Constant.URL_WORDS_FIND_BY_BOOK_AND_UNIT).post(fb).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonWords = response.body().string();
                    Type type = new TypeToken<List<Word>>(){}.getType();
                    List<Word> words = gson.fromJson(jsonWords,type);
                    unit.setWords(words);
                    Message message = new Message();
                    message.what = nowCount++;
                    handler.sendMessage(message);
                }
            });

        }
    }

    private void initView() {
        adapter = new UnitRecyclerAdapter(this, R.layout.fragment_book_detail_item, units, cbChooseAll, llRecite, llDictation);
        rvBookDetail.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);   // 默认设置垂直布局
        rvBookDetail.setLayoutManager(layoutManager);
        Glide.with(this).load(book.getBimgPath()).into(ivCover);
        if (isBind)
            ivBind.setImageDrawable(getResources().getDrawable(R.drawable.binded));
        else
            ivBind.setImageDrawable(getResources().getDrawable(R.drawable.bind_no));

        if (isCollected)
            ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collected));
        else if (!isCollected && isBind)
            ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collected));
        else
            ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collect_no));
    }

    private void findView() {
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
