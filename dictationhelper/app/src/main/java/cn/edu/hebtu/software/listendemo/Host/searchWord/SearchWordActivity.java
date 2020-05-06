package cn.edu.hebtu.software.listendemo.Host.searchWord;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.HistroyWord;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.BookUnitWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.ReadManager;
import cn.edu.hebtu.software.listendemo.Untils.SearchHistoryDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import okhttp3.OkHttpClient;

import static cn.edu.hebtu.software.listendemo.Untils.BookUnitWordDBHelper.TBL_WORD;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.BOOK_UNIT_WORD_DBNAME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.SEARCH_HISTORY_DBNAME;
import static cn.edu.hebtu.software.listendemo.Untils.SearchHistoryDBHelper.TBL_HISTORY;


public class SearchWordActivity extends AppCompatActivity implements View.OnClickListener {

    // 常数相关
    private static final String TYPE_HISTORY = "history"; // 此时显示history
    private static final String TYPE_RESULT = "result";   // 此时显示result
    private static final String TYPE_ONE_RESULT = "one";  // 此时显示单个记录
    private String NOW_TYPE = "history";
    // 历史记录
    private SearchHistoryDBHelper historyDBHelper;
    private SQLiteDatabase historyDB;
    private BookUnitWordDBHelper resultDBHelper;
    private SQLiteDatabase resultDB;
    // 控件声明
    private LinearLayout llSearchShow;
    private EditText etInput;
    private ImageView ivClean;
    private TextView tvExit;
    private RecyclerView rcvHistroy;
    private RecyclerView rcvResult;
    private LinearLayout llWordShowOut;
    private TextView tvWordEnglish;
    private ImageView ivPronunce;
    private ImageView ivWordImg;
    private TextView tvWordChinese;
    private TextView tvClean;
    private ImageView ivWordExit;
    // 数据源
    private List<HistroyWord> histroyWords = new ArrayList<>(); // 历史单词
    private List<Word> wordResult = new ArrayList<>();          // 查询结果
    private Word nowWord = new Word();                             // 当前查询单词

    // 适配器
    private HistroyRecyclerAdapter histroyAdapter = null;
    private ResultRecyclerAdapter resultAdapter = null;

    // 发音
    private ReadManager readManager = new ReadManager(SearchWordActivity.this,"");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_word);
        EventBus.getDefault().register(this);
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.white);
        findViews();
        setListeners();
        initShowTypeIntoHistory();  // 将当前显示状态变成“搜索历史”状态

    }

    private void setListeners() {
        ivClean.setOnClickListener(this);
        ivWordExit.setOnClickListener(this);
        tvExit.setOnClickListener(this);
        ivPronunce.setOnClickListener(this);
        /**
         * 1. et为空时：显示history
         * 2. et有数据、修改数据时：显示result
         * 3. 当et从有数据变成没数据时，显示history
         */
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (!s.toString().equals("")){
                    ivClean.setVisibility(View.GONE);
                }else{
                    ivClean.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")){
                    ivClean.setVisibility(View.GONE);
                }else{
                    ivClean.setVisibility(View.VISIBLE);
                }
                if (NOW_TYPE.equals(TYPE_RESULT) && !s.toString().equals("")) {
                    // 如果当前显示状态为 “搜索结果”，并且现在的字符串不为null
                    // 执行关键字搜索
                    initShowTypeIntoResult(s.toString());
                } else if (NOW_TYPE.equals(TYPE_RESULT) && s.toString().equals("")) {
                    // 如果当前显示状态为“搜索结果”，并且现在的字符串为null
                    // 将显示状态修改为“搜索历史”
                    initShowTypeIntoHistory();
                } else if (NOW_TYPE.equals(TYPE_HISTORY) && !s.toString().equals("")) {
                    // 如果当前显示状态为“搜索历史”，并且现在的字符串不为null
                    // 将状态改为“搜索结果”，并执行搜索
                    initShowTypeIntoResult(s.toString());
                }
            }
        });
    }

    // 初始化“搜索结果”
    private void initShowTypeIntoResult(String selectKey) {
        // 修改样式
        changeViewType(TYPE_RESULT);
        NOW_TYPE = TYPE_RESULT;
        // 获取数据并修改样式
        FindResultDataTask task = new FindResultDataTask();
        task.execute(selectKey);
    }

    // 通过异步任务，修改每次查询数据
    class FindResultDataTask extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (null == resultDBHelper) {
                resultDBHelper = new BookUnitWordDBHelper(SearchWordActivity.this, BOOK_UNIT_WORD_DBNAME, 1);
            }
            if (null == resultDB) {
                resultDB = resultDBHelper.getWritableDatabase();
            }
            wordResult.clear();
        }

        @Override
        protected void onPostExecute(Object words) {
            super.onPostExecute(words);
            if (null == resultAdapter) {
                resultAdapter = new ResultRecyclerAdapter(SearchWordActivity.this, wordResult, R.layout.activity_search_word_result_item, historyDB);
                rcvResult.setAdapter(resultAdapter);
            } else {
                resultAdapter.updateWordList(wordResult);
            }
        }

        @Override
        protected Object doInBackground(Object[] selectKey) {
            Cursor cursor = resultDB.query(TBL_WORD, new String[]{"distinct (wenglish)","wid", "wimgPath","wchinese"}, "wenglish like ? or wchinese like ?", new String[]{selectKey[0].toString()+"%",selectKey[0].toString()+"%"}, "wenglish", null, null);
            if (cursor.moveToFirst()) {
                do {
                    Word word = new Word();
                    word.setWid(cursor.getInt(cursor.getColumnIndex("wid")));
                    word.setWchinese(cursor.getString(cursor.getColumnIndex("wchinese")));
                    word.setWenglish(cursor.getString(cursor.getColumnIndex("wenglish")));
                    word.setWimgPath(cursor.getString(cursor.getColumnIndex("wimgPath")));
                    wordResult.add(word);
                } while (cursor.moveToNext());
            }
            return wordResult;
        }
    }

    // 初始化搜索历史
    private void initShowTypeIntoHistory() {
        changeViewType(TYPE_HISTORY);   // 显示历史记录
        NOW_TYPE = TYPE_HISTORY;
        initHistoryDatas();
        if (histroyWords.size() == 0){
            tvClean.setVisibility(View.GONE);
        }else{
            tvClean.setVisibility(View.VISIBLE);
        }
        if (histroyAdapter == null) {   // 如果当前的显示
            histroyAdapter = new HistroyRecyclerAdapter(this, histroyWords, R.layout.activity_search_word_histroy_item, tvClean, historyDB);
            rcvHistroy.setAdapter(histroyAdapter);
            histroyAdapter.setCleanAllListener();
        } else {
            histroyAdapter.updateWordList(histroyWords);
        }

    }

    // 修改显示界面
    private void changeViewType(String type) {
        switch (type) {
            case TYPE_HISTORY:  // 显示搜索历史
                llSearchShow.setVisibility(View.VISIBLE);
                rcvHistroy.setVisibility(View.VISIBLE);
                ivClean.setVisibility(View.GONE);
                if (histroyWords.size() == 0){
                    tvClean.setVisibility(View.GONE);
                }else{
                    tvClean.setVisibility(View.VISIBLE);
                }
                rcvResult.setVisibility(View.GONE);
                llWordShowOut.setVisibility(View.GONE);
                break;
            case TYPE_RESULT:   // 显示搜索结果集
                llSearchShow.setVisibility(View.VISIBLE);
                rcvHistroy.setVisibility(View.GONE);
                tvClean.setVisibility(View.GONE);
                rcvResult.setVisibility(View.VISIBLE);
                llWordShowOut.setVisibility(View.GONE);
                break;
            case TYPE_ONE_RESULT:// 显示单个结果
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                llSearchShow.setVisibility(View.GONE);
                rcvHistroy.setVisibility(View.GONE);
                tvClean.setVisibility(View.GONE);
                rcvResult.setVisibility(View.GONE);
                llWordShowOut.setVisibility(View.VISIBLE);
                break;
        }
    }

    // 初始化搜索历史
    private void initHistoryDatas() {
        if (historyDBHelper == null) {
            historyDBHelper = new SearchHistoryDBHelper(this, SEARCH_HISTORY_DBNAME, 1);
        }
        if (historyDB == null) {
            historyDB = historyDBHelper.getWritableDatabase();
        }
        histroyWords.clear();
        Cursor cursor = historyDB.query(TBL_HISTORY, null, null, null, null, null, "id desc");
        if (cursor.moveToFirst()) {
            do {
                HistroyWord histroyWord = new HistroyWord();
                histroyWord.setId(cursor.getInt(cursor.getColumnIndex("id")));
                histroyWord.setChinese(cursor.getString(cursor.getColumnIndex("chinese")));
                histroyWord.setEnglish(cursor.getString(cursor.getColumnIndex("english")));
                histroyWord.setWid(cursor.getInt(cursor.getColumnIndex("wid")));
                histroyWords.add(histroyWord);
            } while (cursor.moveToNext());
        }
    }

    // findViewById
    private void findViews() {
        llSearchShow = findViewById(R.id.ll_search_word_search_show);
        etInput = findViewById(R.id.et_search_word_input);
        ivClean = findViewById(R.id.iv_search_word_clean);
        tvExit = findViewById(R.id.tv_search_word_exit);
        rcvHistroy = findViewById(R.id.recy_search_word_histroy);
        rcvResult = findViewById(R.id.recy_search_word_result);
        llWordShowOut = findViewById(R.id.ll_search_word_word_show);
        tvWordEnglish = findViewById(R.id.tv_search_word_english);
        ivPronunce = findViewById(R.id.iv_search_word_pronunce);
        ivWordImg = findViewById(R.id.iv_search_word_image);
        tvWordChinese = findViewById(R.id.tv_search_word_chinese);
        tvClean = findViewById(R.id.tv_search_word_clean);
        ivWordExit = findViewById(R.id.iv_search_word_word_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        historyDB.close();
        historyDBHelper.close();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !NOW_TYPE.equals(TYPE_ONE_RESULT)) {
            finish();
            overridePendingTransition(R.anim.rev_in_search_from_down, R.anim.rev_out_search_to_up);
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && NOW_TYPE.equals(TYPE_ONE_RESULT)){
            initShowTypeIntoHistory();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search_word_clean:
                etInput.setText("");
                break;
            case R.id.iv_search_word_word_exit:
                initShowTypeIntoHistory();  // 转换为浏览搜索历史的页面
                break;
            case R.id.tv_search_word_exit:
                finish();
                overridePendingTransition(R.anim.rev_in_search_from_down, R.anim.rev_out_search_to_up);
                break;
            case R.id.iv_search_word_pronunce:
                // 进行发音
                readManager.pronounce(nowWord.getWenglish());
                break;
        }
    }

    // 展示界面结果
    private void setOneResultShow() {
        etInput.setText("");
        tvWordChinese.setText(nowWord.getWchinese());
        tvWordEnglish.setText(nowWord.getWenglish());
        RequestOptions options = new RequestOptions()//.placeholder(placeholder == 0 ? R.drawable.img_loading : placeholder)
                .skipMemoryCache(false)  //用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有图片(原图,转换图)
                .fitCenter()   //fitCenter 缩放图片充满ImageView CenterInside大缩小原(图) CenterCrop大裁小扩充满ImageView  Center大裁(中间)小原
                .error(R.drawable.error);
        Glide.with(this).load(nowWord.getWimgPath()).apply(options).thumbnail(Glide.with(this).load(R.drawable.wait2)).into(ivWordImg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getShowWordFromResult(Word word) {
        this.nowWord = word;
        // 修改展示界面
        changeViewType(TYPE_ONE_RESULT);
        NOW_TYPE = TYPE_ONE_RESULT;
        setOneResultShow();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getShowWordFromHistroy(HistroyWord word) {
        if (null == resultDBHelper) {
            resultDBHelper = new BookUnitWordDBHelper(SearchWordActivity.this, BOOK_UNIT_WORD_DBNAME, 1);
        }
        if (null == resultDB) {
            resultDB = resultDBHelper.getWritableDatabase();
        }
        Cursor cursor = resultDB.query(TBL_WORD, new String[]{"wid", "wchinese", "wenglish", "wimgPath"}, "wid = ?", new String[]{word.getWid() + ""}, null, null, null);
        if (cursor.moveToFirst()) {
            nowWord.setWid(cursor.getInt(cursor.getColumnIndex("wid")));
            nowWord.setWchinese(cursor.getString(cursor.getColumnIndex("wchinese")));
            nowWord.setWenglish(cursor.getString(cursor.getColumnIndex("wenglish")));
            nowWord.setWimgPath(cursor.getString(cursor.getColumnIndex("wimgPath")));
        }
        // 修改展示界面
        changeViewType(TYPE_ONE_RESULT);
        NOW_TYPE = TYPE_ONE_RESULT;
        setOneResultShow();
    }
}
