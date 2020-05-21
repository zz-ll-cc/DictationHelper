package cn.edu.hebtu.software.listendemo.Host.index;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.hebtu.software.listendemo.Entity.Book;
import cn.edu.hebtu.software.listendemo.Entity.Unit;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.bookDetail.BookDetailActivity;
import cn.edu.hebtu.software.listendemo.Host.searchBook.SearchBookActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.BookUnitWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static cn.edu.hebtu.software.listendemo.Host.bookDetail.BookDetailActivity.POST_FROM_BOOK_DETAIL;
import static cn.edu.hebtu.software.listendemo.Untils.BookUnitWordDBHelper.TBL_BOOK;
import static cn.edu.hebtu.software.listendemo.Untils.BookUnitWordDBHelper.TBL_UNIT;
import static cn.edu.hebtu.software.listendemo.Untils.BookUnitWordDBHelper.TBL_WORD;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.BOOK_JSON;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.BOOK_UNIT_WORD_DBNAME;

public class HostFragment extends Fragment {
    private static final int GET_WORDS = 2000;
    private static final int GET_UNITS = 2001;
    private LinearLayout llFindBooks;
    private LinearLayout llContinueStudy;
    private List<Book> res = new ArrayList<>();
    private List<Word> initWords;
    private HostRecyclerAdapter adapter = null;
    private RecyclerView recyclerView = null;
    private SharedPreferences sp = ListenIndexActivity.activity.getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
    ;
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private View view;
    private LinearLayout llOut;
    private static final int GET_BOOKS = 1;
    @BindView(R.id.tv_continue)
    TextView tvContinue;
    private Banner banner;

    private List<String> mTitleList = new ArrayList<>();
    private List<Integer> mImgList = new ArrayList<>();

    // 书的sqlite
    private BookUnitWordDBHelper bookDBHelper;
    private SQLiteDatabase bookDB;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_BOOKS: // 获取所有书
                    initView(view);
                    makeBookData2DB();
                    initRecyView();
                    setListener();
                    break;
                case GET_WORDS: // 获取所有单词
                    makeWordData2DB();
                    initData();
                    initRecyView();
                    setListener();
                    break;
                case GET_UNITS:
                    makeUnitData2DB((List<Unit>) msg.obj);
                    initData();
                    initRecyView();
                    setListener();
                    break;
            }
        }
    };

    private void makeUnitData2DB(List<Unit> units) {
        for (Unit unit : units) {
            ContentValues cv = new ContentValues();
            cv.put("bid",unit.getBid());
            cv.put("unid",unit.getUnid());
            cv.put("type",unit.getType());
            cv.put("cost",unit.getCost());
            cv.put("unName",unit.getUnName());
            cv.put("deleted",unit.getDeleted());
            cv.put("version",unit.getVersion());
            cv.put("createTime", unit.getCreateTime());
            cv.put("updateTime", unit.getUpdateTime());
            bookDB.insert(TBL_UNIT,null,cv);
        }
    }

    /**
     * 将单词初始数据写入sqlite
     */
    private void makeWordData2DB() {
        for (Word word : initWords) {
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
            bookDB.insert(TBL_WORD, null, cv);
        }
    }

    /**
     * 将相关数据写入db
     */
    private void makeBookData2DB() {
        for (Book book : res) {
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
            int exists = bookDB.update(TBL_BOOK, cv, "bid = ?", new String[]{book + ""});
            if (exists == 0) {
                bookDB.insert(TBL_BOOK, null, cv);
            }
        }
    }

    private OkHttpClient client = new OkHttpClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_host, container, false);
        bookDBHelper = new BookUnitWordDBHelper(ListenIndexActivity.activity, BOOK_UNIT_WORD_DBNAME, 1);
        bookDB = bookDBHelper.getWritableDatabase();
        EventBus.getDefault().register(this);
        ButterKnife.bind(this, view);
        initView(view);
        getInitWords();

        initData();
        // 设置轮播图
        BannerSet();
        initRecyView();
        setListener();
        return view;
    }

    private void getInitWords() {
        long wordCount = -1;
        Cursor cursor = bookDB.query(TBL_WORD, new String[]{"count(wid) wordCount"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            wordCount = cursor.getLong(cursor.getColumnIndex("wordCount"));
        }
        if (wordCount == 0) {
            FormBody fb = new FormBody.Builder().build();
            Request request = new Request.Builder().url(Constant.URL_GET_INIT_ALL_WORD).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonBooks = response.body().string();
                    Message message = new Message();
                    message.what = GET_WORDS;
                    Type type = new TypeToken<List<Word>>() {
                    }.getType();
                    initWords = gson.fromJson(jsonBooks, type);
                    handler.sendMessage(message);
                }
            });
        }
    }

    private void setListener() {
        llFindBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchBookActivity.class);
                startActivity(intent);
            }
        });
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookJson = sp.getString(BOOK_JSON, "");
                if ("".equals(bookJson)) {
                    Toast.makeText(getContext(), "没有找到当前的学习记录", Toast.LENGTH_SHORT).show();
                } else {
                    Book book = gson.fromJson(bookJson, Book.class);
                    Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constant.HOST_CON_DETAIL_BOOK, book);
                    intent.putExtras(bundle);
                    getContext().startActivity(intent);
                }
            }
        });
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recv_fragment_host);
        llContinueStudy = view.findViewById(R.id.ll_fragment_host_continueStudy);
        llFindBooks = view.findViewById(R.id.ll_fragment_host_findBooks);
        llOut = view.findViewById(R.id.ll_fragment_host_out);
        banner = view.findViewById(R.id.play_banner);
    }

    private void initRecyView() {
        adapter = new HostRecyclerAdapter(R.layout.fragment_host_recycler_item, res, getContext(), sp);
        recyclerView.setAdapter(adapter);
    }

    private void initData() {
        sp = getContext().getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        // 先对本地数据库进行查询所有操作
        Cursor cursor = bookDB.query(TBL_BOOK, null, null, null, null, null, null);
        Cursor cursor1 = bookDB.query(TBL_UNIT, null, null, null, null, null, null);
        // 此时有数据，不用去进行网络请求
        if (cursor.moveToFirst()) {
            res.clear();
            do {
                Book book = new Book();
                book.setBid(cursor.getInt(cursor.getColumnIndex("bid")));
                book.setBimgPath(cursor.getString(cursor.getColumnIndex("bimgPath")));
                book.setBname(cursor.getString(cursor.getColumnIndex("bname")));
                book.setBunitAccount(cursor.getInt(cursor.getColumnIndex("bunitAccount")));
                book.setBvid(cursor.getInt(cursor.getColumnIndex("bvid")));
                book.setGid(cursor.getInt(cursor.getColumnIndex("gid")));
                book.setBookWordVersion(cursor.getInt(cursor.getColumnIndex("bookWordVersion")));
                book.setCreateTime(cursor.getString(cursor.getColumnIndex("createTime")));
                book.setUpdateTime(cursor.getString(cursor.getColumnIndex("updateTime")));
                book.setDeleted(cursor.getInt(cursor.getColumnIndex("deleted")));
                book.setVersion(cursor.getInt(cursor.getColumnIndex("version")));
                res.add(book);
            } while (cursor.moveToNext());
        } else { // 此时数据库里边没有相关数据，需要发送网络请求获取
            FormBody fb = new FormBody.Builder().build();
            Request request = new Request.Builder().url(Constant.URL_BOOKS_FIND_ALL).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonBooks = response.body().string();
                    Message message = new Message();
                    message.what = GET_BOOKS;
                    Type type = new TypeToken<List<Book>>() {
                    }.getType();
                    Log.e("getBooks",gson.fromJson(jsonBooks,type).toString());
                    res = gson.fromJson(jsonBooks, type);
                    handler.sendMessage(message);
                }
            });
        }
        if (!cursor1.moveToFirst()) {    // 此时没有装填unit的数据
            FormBody fb = new FormBody.Builder().build();
            Request request = new Request.Builder().url(Constant.URL_GET_INIT_ALL_UNIT).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonUnits = response.body().string();
                    Message message = new Message();
                    message.what = GET_UNITS;
                    Type type = new TypeToken<List<Unit>>() {
                    }.getType();
                    Log.e("getUnits",gson.fromJson(jsonUnits,type).toString());
                    message.obj = gson.fromJson(jsonUnits, type);
                    handler.sendMessage(message);
                }
            });
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(getActivity(), R.color.backgray);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        banner.stopAutoPlay();
        bookDB.close();
        bookDBHelper.close();
    }

    public static final String CHANGE_FROM = "changeFrom";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeBookWordVersionRes(Map<String, Object> map) {
        switch (map.get(CHANGE_FROM).toString()) {
            case POST_FROM_BOOK_DETAIL:
                for (int i = 0; i < res.size(); i++) {
                    Book book = res.get(i);
                    if (book.getBid() == (int) map.get("bid")) {
                        book.setBookWordVersion((int) map.get("bookWordVersion"));
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initView(view);
        initRecyView();
        StatusBarUtil.setStatusBarColor(getActivity(), R.color.backgray);
    }

    private void BannerSet() {
        mImgList.clear();
        mImgList.add(R.drawable.banner1);
        mImgList.add(R.drawable.banner2);
        mTitleList.clear();
        for (int i = 0; i < mImgList.size(); i++) {
//            mTitleList.add("第" + i + "张图片");
            if (i == 0) {
                mTitleList.add("好好学习");
            } else if (i == 1) {
                mTitleList.add("天天向上");
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            banner.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 30);
                }
            });
            banner.setClipToOutline(true);
        }
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE); // 显示圆形指示器和标题（水平显示
        //设置图片加载器
        banner.setImageLoader(new MyLoader());
        //设置图片集合
        banner.setImages(mImgList);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(mTitleList);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        // setOnBannerClickListener  1.4.9 以后就废弃了 。  setOnBannerListener 是1.4.9以后使用。
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Log.e("tt", "第" + position + "张轮播图点击了！");
                //Toast.makeText(this,"",Toast.LENGTH_LONG).show();
                //UIUtils.showToast("第" + position + "张轮播图点击了！");
            }
        });


    }

    // 图片加载器
    public class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }

    }

}
