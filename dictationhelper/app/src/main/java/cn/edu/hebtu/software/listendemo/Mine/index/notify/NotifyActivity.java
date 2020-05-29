package cn.edu.hebtu.software.listendemo.Mine.index.notify;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Message;
import cn.edu.hebtu.software.listendemo.Entity.MessageJson;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Mine.index.credit.SpacesItemDecoration;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.MessageDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.edu.hebtu.software.listendemo.Untils.Constant.DEFAULT_KEEP_USER;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.MESSAGE_DB_NAME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.MESSAGE_VERSION_KEEP;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.SP_NAME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.USER_KEEP_KEY;
import static cn.edu.hebtu.software.listendemo.Untils.MessageDBHelper.TABLE_MESSAGE;

public class NotifyActivity extends AppCompatActivity implements View.OnClickListener {
    // 常量
    private static final int GET_NEW_UPD_MESSAGE = 100;
    private static final int GET_READ_LIST = 101;
    // 控件
    private ImageView ivExit;
    private SmartRefreshLayout smart;
    private RecyclerView rcvMessage;
    private RelativeLayout rlOut;
    // 工具类
    private SharedPreferences sp;
    private MessageDBHelper dbHelper;
    private SQLiteDatabase messageDB;
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();
    // 适配器
    private NotifyRecyclerAdapter adapter;
    // 数据源
    private User user;
    private List<Message> messageList;  // 消息数据
    private int messageVersion;         // 消息版本信息
    private List<Integer> hitList;      // 以点击的数据
    // handler
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_NEW_UPD_MESSAGE:
                    MessageJson messageJson = (MessageJson) msg.obj;
                    int status = (int) messageJson.getStatus();
                    switch (status) {
                        case 0:
                            // 从新获取的数据
                            messageList = messageJson.getMessageList();
                            makeMessageIntoDB();
                            messageVersion = messageJson.getMessageVersion();
                            sp.edit().putInt(MESSAGE_VERSION_KEEP, messageVersion).commit();
                            break;
                        case 1:
                            // 从数据库中获取相关数据
                            getDatasFromDB();
                            break;
                        case 2:
                            // 从新获取的数据
                            messageList = messageJson.getMessageList();
                            makeMessageIntoDB();
                            messageVersion = messageJson.getMessageVersion();
                            sp.edit().putInt(MESSAGE_VERSION_KEEP, messageVersion).commit();
                            break;
                    }
                    // 获取是否点击
                    getHitedList();
                    break;
                case GET_READ_LIST:
                    String jsonStr = (String) msg.obj;
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    hitList = gson.fromJson(jsonStr, type);
                    if (hitList != null) {
                        for (int i = 0; i < hitList.size(); i++) {
                            for (int j = 0; j < messageList.size(); j++) {
                                if (hitList.get(i) == messageList.get(j).getId()) {
                                    messageList.get(j).setHits(-1);
                                    continue;
                                }
                            }
                        }
                    }
                    adapter = new NotifyRecyclerAdapter(messageList, NotifyActivity.this, R.layout.activity_notify_recycler_item, user);
                    rcvMessage.setAdapter(adapter);
                    smart.finishRefresh();
                    break;
            }
        }
    };

    // 获取用户的点击列表
    private void getHitedList() {
        FormBody fb = new FormBody.Builder().add("userId", user.getUid() + "").build();
        Request request = new Request.Builder()
                .url(Constant.URL_GET_READ_RECORD)
                .post(fb).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                android.os.Message message = new android.os.Message();
                message.what = GET_READ_LIST;
                message.obj = jsonStr;
                handler.sendMessage(message);
            }
        });
    }

    // 从数据库里获取数据
    private void getDatasFromDB() {
        if (null == dbHelper) {
            dbHelper = new MessageDBHelper(this, MESSAGE_DB_NAME, 1);
        }
        if (null == messageDB) {
            messageDB = dbHelper.getWritableDatabase();
        }
        Cursor cursor = messageDB.query(TABLE_MESSAGE, null, null, null, null, null, "CREATETIME desc");
        if (cursor.moveToFirst()) {
            messageList = new ArrayList<>();
            do {
                Message message = new Message();
                message.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                message.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
                message.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
                message.setSubtitle(cursor.getString(cursor.getColumnIndex("SUBTITLE")));
                message.setTitleImage(cursor.getString(cursor.getColumnIndex("TITLEIMAGE")));
                message.setTypeName(cursor.getString(cursor.getColumnIndex("TYPENAME")));
                message.setCreateTime(cursor.getString(cursor.getColumnIndex("CREATETIME")));
                message.setUpdateTime(cursor.getString(cursor.getColumnIndex("UPDATETIME")));
                message.setHits(cursor.getInt(cursor.getColumnIndex("HITS")));
                message.setDeleted(cursor.getInt(cursor.getColumnIndex("DELETED")));
                messageList.add(message);
            } while (cursor.moveToNext());
        }
    }

    // 将数据填到数据库里
    private void makeMessageIntoDB() {
        if (null == dbHelper) {
            dbHelper = new MessageDBHelper(this, MESSAGE_DB_NAME, 1);
        }
        if (null == messageDB) {
            messageDB = dbHelper.getWritableDatabase();
        }
        for (int i = 0; i < messageList.size(); i++) {
            Message message = messageList.get(i);
            ContentValues cv = new ContentValues();
            cv.put("TITLEIMAGE", message.getTitleImage());
            cv.put("TITLE", message.getTitle());
            cv.put("SUBTITLE", message.getSubtitle());
            cv.put("CONTENT", message.getContent());
            cv.put("TYPENAME", message.getTypeName());
            cv.put("HITS", message.getHits());
            cv.put("DELETED", message.getDeleted());
            cv.put("CREATETIME", message.getCreateTime());
            cv.put("UPDATETIME", message.getUpdateTime());
            if (messageDB.update(TABLE_MESSAGE, cv, "ID = ?", new String[]{message.getId() + ""}) == 0) {
                cv.put("ID", message.getId());
                messageDB.insert(TABLE_MESSAGE, null, cv);
            }
        }
    }

    // 修改点击显示
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeMessageClicked(Map<String, Object> map) {
        if (map.get("result").toString().equals("ok")) {
            int messageId = Integer.parseInt(map.get("itemId").toString());
            for (int i = 0; i < messageList.size(); i++) {
                if (messageList.get(i).getId() == messageId) {
                    messageList.get(i).setHits(-1);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        EventBus.getDefault().register(this);
        StatusBarUtil.setStatusBarColor(this, R.color.backgray);
        StatusBarUtil.statusBarLightMode(this);
        findViews();
        initDatas();
        marginTopStateBar();
        setLiteners();
    }

    private void marginTopStateBar() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = StatusBarUtil.getStatusBarHeight(this);
        rlOut.setLayoutParams(layoutParams);
    }

    private void setLiteners() {
        ivExit.setOnClickListener(this::onClick);
        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getNewMessage();
            }
        });
    }

    private void initDatas() {
        dbHelper = new MessageDBHelper(this, MESSAGE_DB_NAME, 1);
        messageDB = dbHelper.getWritableDatabase();
        sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        user = gson.fromJson(sp.getString(USER_KEEP_KEY, DEFAULT_KEEP_USER), User.class);
        messageVersion = sp.getInt(MESSAGE_VERSION_KEEP, 0);
        getNewMessage();
    }

    /**
     * 获取最新消息
     */
    private void getNewMessage() {
        FormBody fb = null;
        if (messageVersion != 0) {
            fb = new FormBody.Builder().add("version", messageVersion + "").build();
        }
        Request.Builder requestBuilder = new Request.Builder()
                .url(Constant.URL_GET_UPD_NOTIFY);
        if (fb != null) {
            requestBuilder.post(fb);
        }
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                MessageJson messageJson = gson.fromJson(jsonStr, MessageJson.class);
                android.os.Message message = new android.os.Message();
                message.what = GET_NEW_UPD_MESSAGE;
                message.obj = messageJson;
                handler.sendMessage(message);
            }
        });
    }

    private void findViews() {
        ivExit = findViewById(R.id.iv_notify_msg_exit);
        smart = findViewById(R.id.smart_notify);
        rlOut = findViewById(R.id.rl_notify_out);
        rcvMessage = findViewById(R.id.rcv_notify);
        smart.setEnableLoadMore(false); // 禁用获取更多
        smart.setRefreshHeader(new MaterialHeader(this));
        rcvMessage.addItemDecoration(new SpacesItemDecoration(20));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_notify_msg_exit:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        dbHelper.close();
        messageDB.close();
    }
}
