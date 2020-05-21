package cn.edu.hebtu.software.listendemo.Host.bookDetail;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Book;
import cn.edu.hebtu.software.listendemo.Entity.EventInfo;
import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.Unit;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.BookUnitWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.CorrectWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.edu.hebtu.software.listendemo.Host.index.HostFragment.CHANGE_FROM;
import static cn.edu.hebtu.software.listendemo.Untils.BookUnitWordDBHelper.TBL_BOOK;
import static cn.edu.hebtu.software.listendemo.Untils.BookUnitWordDBHelper.TBL_UNIT;
import static cn.edu.hebtu.software.listendemo.Untils.BookUnitWordDBHelper.TBL_WORD;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.BOOK_UNIT_WORD_DBNAME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.SP_NAME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.URL_GET_ACCOUNT;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.USER_KEEP_KEY;

public class BookDetailActivity extends AppCompatActivity {
    public static final String POST_FROM_BOOK_DETAIL = "fromBookDetail";
    private static final int nowCount = 100;
    private static final int requestCount = 0;
    private static final int GET_VERSION_CHANGE = 10086;
    private static final int GET_VERSION_CHANGE_FALSE = 20086;
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
    private Map<Integer, List<Integer>> colMap;
    private User user;
    private OkHttpClient client = new OkHttpClient();
    private NumberProgressBar pbLearn;
    private NumberProgressBar pbListen;
    private SQLiteDatabase sqLiteDatabase;
    // 记录单词以及单元信息
    private BookUnitWordDBHelper dbHelper;
    private SQLiteDatabase wordDB;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case requestCount:
                    Log.e("requestCount", requestCount + "");
                    List<Word> words1 = (List<Word>) msg.obj;
                    keepNewWords(words1);
                    initView();
                    setListener();
                    break;
                case nowCount:
                    Log.e("nowCount", nowCount + "");
                    List<Word> words = (List<Word>) msg.obj;
                    keepNewWords(words);
                    initView();
                    setListener();
                    break;
                case GET_VERSION_CHANGE:
                    // 2. 更新书信息
                    updateBook();
                    // 2. 装填新单词
                    List<Word> words0 = (List<Word>) msg.obj;
                    keepNewWords(words0);
                    initView();
                    setListener();
                    break;
            }
        }
    };

    private void updateBook() {
        ContentValues cv = new ContentValues();
        cv.put("bid", book.getBid());
        cv.put("bvid", book.getBvid());
        cv.put("bimgPath", book.getBimgPath());
        cv.put("gid", book.getGid());
        cv.put("bname", book.getBname());
        cv.put("bookWordVersion", book.getBookWordVersion());
        cv.put("bunitAccount", book.getBunitAccount());
        cv.put("createTime", book.getCreateTime());
        cv.put("updateTime", book.getUpdateTime());
        cv.put("version", book.getVersion());
        cv.put("deleted", book.getDeleted());
        int col = wordDB.update(TBL_BOOK, cv, "bid = ?", new String[]{book.getBid() + ""});
        if (col == 0) {
            wordDB.insert(TBL_BOOK, null, cv);
        }
    }

    private void keepNewWords(List<Word> words) {
        for (Word word : words) {
            Log.e("插入单词", word.toString());
            ContentValues cv = new ContentValues();
            cv.put("wid", word.getWid());
            cv.put("bid", word.getBid());
            cv.put("wenglish", word.getWenglish());
            cv.put("wchinese", word.getWchinese());
            cv.put("unid", word.getUnid());
            cv.put("type", word.getType());
            cv.put("wimgPath", word.getWimgPath());
            cv.put("version", word.getVersion());
            cv.put("deleted", word.getDeleted());
            cv.put("createTime", word.getCreateTime());
            cv.put("updateTime", word.getUpdateTime());
            wordDB.insert(TBL_WORD, null, cv);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        EventBus.getDefault().register(this);
        dbHelper = new BookUnitWordDBHelper(this, BOOK_UNIT_WORD_DBNAME, 1);
        wordDB = dbHelper.getWritableDatabase();
        findView();
        initData();

        StatusBarUtil.statusBarLightMode(this);
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
                                Type type = new TypeToken<Map<Integer, Integer>>() {
                                }.getType();
                                Map<Integer, Integer> bindMap = gson.fromJson(sp.getString(Constant.BIND_KEY, Constant.DEFAULT_BING_MAP), type);
                                bindMap.put(user.getUid(), Constant.DEFAULT_BIND_ID);

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(Constant.BIND_KEY, gson.toJson(bindMap));
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
                                colMap.put(user.getUid(), colList);
                                editor.putString(Constant.COLLECT_KEY, gson.toJson(colMap));
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
                                // 选中“确定”按钮，选择绑定
                                // 更改SharedP中数据
                                Type type = new TypeToken<Map<Integer, Integer>>() {
                                }.getType();
                                Map<Integer, Integer> bindMap = gson.fromJson(sp.getString(Constant.BIND_KEY, Constant.DEFAULT_BING_MAP), type);
                                bindMap.put(user.getUid(), book.getBid());

                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(Constant.BIND_KEY, gson.toJson(bindMap));

                                // 修改显示样式
                                ivBind.setImageDrawable(getResources().getDrawable(R.drawable.binded));
                                ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collected));
                                // 修改收藏列表
                                colList.add(book.getBid());
                                colMap.put(user.getUid(), colList);
                                editor.putString(Constant.COLLECT_KEY, gson.toJson(colMap));
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
                        colMap.put(user.getUid(), colList);
                        editor.putString(Constant.COLLECT_KEY, gson.toJson(colMap));
                        editor.commit();
                        isCollected = false;
                        ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collect_no));
                    } else {
                        // 当前为未收藏
                        SharedPreferences.Editor editor = sp.edit();
                        colList.add(book.getBid());
                        colMap.put(user.getUid(), colList);
                        editor.putString(Constant.COLLECT_KEY, gson.toJson(colMap));
                        editor.commit();
                        isCollected = true;
                        ivCollect.setImageDrawable(getResources().getDrawable(R.drawable.collected));
                    }
                    break;
            }
        }
    }

    private List<Word> getDataFromLocal(Unit unit) {
        Cursor cursor = wordDB.query(TBL_WORD, null, "bid = ? and unid = ?", new String[]{book.getBid() + "", unit.getUnid() + ""}, null, null, null);
        List<Word> words = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Word word = new Word();
                word.setBid(cursor.getInt(cursor.getColumnIndex("bid")));
                word.setWid(cursor.getInt(cursor.getColumnIndex("wid")));
                word.setWchinese(cursor.getString(cursor.getColumnIndex("wchinese")));
                word.setWenglish(cursor.getString(cursor.getColumnIndex("wenglish")));
                word.setWimgPath(cursor.getString(cursor.getColumnIndex("wimgPath")));
                word.setType(cursor.getInt(cursor.getColumnIndex("type")));
                word.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
                word.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
                word.setUnid(cursor.getInt(cursor.getColumnIndex("unid")));
                word.setVersion(cursor.getInt(cursor.getColumnIndex("version")));
                word.setDeleted(cursor.getInt(cursor.getColumnIndex("deleted")));
                words.add(word);
            } while (cursor.moveToNext());
        }
        Log.e("数据库获取", "aaaa");
        return words;
    }

    private void initData() {
        book = (Book) getIntent().getSerializableExtra(Constant.HOST_CON_DETAIL_BOOK);
//        checkVersion(book.getBid(),book.getVersion());
        tvName.setText(book.getBname());
        tvName.setSelected(true);
        sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        String userStr = sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER);
        List<UnLock> unLocks = null;
        try {
            JSONObject jsonObject = new JSONObject(userStr);
            String unLockList = jsonObject.get("unlockList").toString();
            Type type = new TypeToken<List<UnLock>>(){}.getType();
            unLocks = new Gson().fromJson(unLockList,type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user = gson.fromJson(userStr, User.class);
        user.setUnLockList(unLocks);
        Type type = new TypeToken<Map<Integer, List<Integer>>>() {
        }.getType();
        colMap = gson.fromJson(sp.getString(Constant.COLLECT_KEY, Constant.DEFAULT_COLLECT_LIST), type);
        if (colMap.containsKey(user.getUid()))
            colList = colMap.get(user.getUid());
        else
            colList = new ArrayList<>();
        if (colList.contains(book.getBid()))
            isCollected = true;
        Type type1 = new TypeToken<Map<Integer, Integer>>() {
        }.getType();
        Map<Integer, Integer> bindMap = gson.fromJson(sp.getString(Constant.BIND_KEY, Constant.DEFAULT_BING_MAP), type1);
        int bindId = -1;
        if (bindMap.containsKey(user.getUid()))
            bindId = bindMap.get(user.getUid());
        else
            bindId = Constant.DEFAULT_BIND_ID;
        if (bindId == book.getBid())
            isBind = true;
        // 1. 根据单元数 创建这本书的单元List
        getBookUnits();
        checkBookDetailVersion();
        // 根据单元，查单词
        selectAllWordByUnit();
        //记录在本地这本书
        sp.edit().putString(Constant.BOOK_JSON, gson.toJson(book)).commit();
    }

    private void getBookUnits() {
        Cursor cursor = wordDB.query(TBL_UNIT, null, "bid = ?", new String[]{book.getBid() + ""}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Unit unit = new Unit();
                unit.setBid(cursor.getInt(cursor.getColumnIndex("bid")));
                unit.setUnid(cursor.getInt(cursor.getColumnIndex("unid")));
                unit.setType(cursor.getInt(cursor.getColumnIndex("type")));
                unit.setCost(cursor.getInt(cursor.getColumnIndex("cost")));
                Log.e("untiCost",unit.getCost()+"");
                unit.setUnName(cursor.getString(cursor.getColumnIndex("unName")));
                unit.setDeleted(cursor.getInt(cursor.getColumnIndex("deleted")));
                unit.setVersion(cursor.getInt(cursor.getColumnIndex("version")));
                unit.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
                unit.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
                units.add(unit);
            }
            while (cursor.moveToNext());
        }
    }

    private void selectAllWordByUnit() {
        for (final Unit unit : units) {
            Cursor cursor = wordDB.query(TBL_WORD, null, "bid = ? and unid = ?", new String[]{book.getBid() + "", unit.getUnid() + ""}, null, null, null);
            if (cursor.moveToFirst()) {
                List<Word> words = new ArrayList<>();
                do {
                    Word word = new Word();
                    word.setBid(cursor.getInt(cursor.getColumnIndex("bid")));
                    word.setWid(cursor.getInt(cursor.getColumnIndex("wid")));
                    word.setWchinese(cursor.getString(cursor.getColumnIndex("wchinese")));
                    word.setWenglish(cursor.getString(cursor.getColumnIndex("wenglish")));
                    word.setWimgPath(cursor.getString(cursor.getColumnIndex("wimgPath")));
                    word.setType(cursor.getInt(cursor.getColumnIndex("type")));
                    word.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
                    word.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
                    word.setUnid(cursor.getInt(cursor.getColumnIndex("unid")));
                    word.setVersion(cursor.getInt(cursor.getColumnIndex("version")));
                    word.setDeleted(cursor.getInt(cursor.getColumnIndex("deleted")));
                    words.add(word);
                } while (cursor.moveToNext());
                unit.setWords(words);
                EventBus.getDefault().post("fromDatabase");
            } else {    // 进行网络请求，获取数据
                FormBody fb = new FormBody.Builder().add("bid", book.getBid() + "").add("unid", unit.getUnid() + "").build();
                Request request = new Request.Builder().url(Constant.URL_WORDS_FIND_BY_BOOK_AND_UNIT).post(fb).build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        List<Word> words = getDataFromLocal(unit);
                        unit.setWords(words);
                        Message message = new Message();
                        message.what = nowCount;
                        message.obj = words;
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String jsonWords = response.body().string();
                        Type type = new TypeToken<List<Word>>() {
                        }.getType();
                        List<Word> words = gson.fromJson(jsonWords, type);
                        unit.setWords(words);
                        Message message = new Message();
                        message.what = nowCount;
                        message.obj = words;
                        handler.sendMessage(message);
                    }
                });
            }
        }
    }

    private void checkBookDetailVersion() {
        Log.e("bookVersion",book.getBookWordVersion()+"");
        FormBody fb = new FormBody.Builder().add("bid", book.getBid() + "").add("version", book.getBookWordVersion() + "").build();
        Request request = new Request.Builder().url(Constant.URL_CHECK_BOOK_DETAIL_VERSION).post(fb).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonWords = response.body().string();
                try {
                    JSONObject jsonStr = new JSONObject(jsonWords);
                    EventBus.getDefault().post(jsonStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void CheckBookVersion(JSONObject jsonStr) {
        int type = 0;
        try {
            type = jsonStr.getInt("type");
            Log.e("typeeee", type + "");
            switch (type) {
                case 0:
                    // 此时未进行更改
                    selectAllWordByUnit();
                    break;
                case 1:
                    // 此时进行了更改
                    // 1. 修改对应书的version
                    book.setBookWordVersion(jsonStr.getInt("version"));
                    Map<String, Object> postMap = new HashMap<>();
                    postMap.put(CHANGE_FROM, POST_FROM_BOOK_DETAIL);
                    postMap.put("bid", book.getBid());
                    postMap.put("bookWordVersion", book.getBookWordVersion());
                    EventBus.getDefault().post(postMap);
                    updateBook();
                    // 2. 修改单词
                    Type type1 = new TypeToken<List<Word>>() {
                    }.getType();
                    List<Word> words = gson.fromJson(jsonStr.get("word").toString(), type1);
                    keepNewWords(words);
                    // 3. 装填单词
                    selectAllWordByUnit();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        adapter = new UnitRecyclerAdapter(this, R.layout.fragment_book_detail_item, units, cbChooseAll, llRecite, llDictation, user);
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
        pbLearn.setMax(100);
        pbLearn.setProgress(0);
        pbListen.setMax(100);
        pbListen.setProgress(0);
        askForAccount();


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
        pbLearn = findViewById(R.id.pb_learn);
        pbListen = findViewById(R.id.pb_listen);
    }

    private void askForAccount() {
        FormBody body = new FormBody.Builder().add("bid", book.getBid() + "").build();
        Request request = new Request.Builder().url(URL_GET_ACCOUNT).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                EventInfo<String, String, Object> eventInfo = new EventInfo<>();
                Map<String, String> map = new HashMap<>();
                map.put("count", response.body().string());
                eventInfo.setContentString("receiveCount");
                eventInfo.setContentMap(map);
                EventBus.getDefault().post(eventInfo);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiver(EventInfo eventInfo) {
        if ("receiveCount".equals(eventInfo.getContentString())) {
            String count = (String) eventInfo.getContentMap().get("count");
            int accout = Integer.parseInt(count);
            //数据库查询计算比例
            int correct = getListenProgress();
            double result = correct * 1.0 / accout * 100;
            Log.e("1,2", "" + result);
            pbListen.setProgress((int) Math.round(result));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initView(String from) {
        switch (from) {
            case "fromDatabase":
                initView();
                setListener();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        dbHelper.close();
        wordDB.close();
    }


    public int getListenProgress() {
        CorrectWordDBHelper currectWordDBHelper = new CorrectWordDBHelper(this, "tbl_correctWord.db", 1);
        sqLiteDatabase = currectWordDBHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("TBL_CURRECTWORD", null, "BID=?", new String[]{book.getBid() + ""}, null, null, null);
        int correct = 0;
        if (cursor.moveToFirst()) {
            do {
                correct += 1;
            } while (cursor.moveToNext());
        }
        Log.e("正确的", "" + correct);
        return correct;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUnLockMsg(Map<String, Object> postMap) {
        switch (postMap.get("type").toString()) {
            case "fromUnLockUnit":
                Unit unit = (Unit) postMap.get("unit");
                String result = postMap.get("result").toString();
                Toast.makeText(this, "解锁" + result, Toast.LENGTH_SHORT).show();
                updateUser(user.getUid());
                break;
        }
    }
    // 更新用户信息
    private void updateUser(int userId) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("id", userId + "").build();
        Request request = new Request.Builder().url(Constant.URL_UPDATE_MYSELF).post(fb).build();
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
                String json = response.body().string();
                List<UnLock> unLocks = null;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String unLockList = jsonObject.get("unlockList").toString();
                    Type type = new TypeToken<List<UnLock>>(){}.getType();
                    unLocks = new Gson().fromJson(unLockList,type);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                user = gson.fromJson(json,User.class);
                user.setUnLockList(unLocks);
                getSharedPreferences(SP_NAME,MODE_PRIVATE).edit().putString(USER_KEEP_KEY,json).commit();
                adapter.notifyDataSetChanged();
            }
        });
    }
}
