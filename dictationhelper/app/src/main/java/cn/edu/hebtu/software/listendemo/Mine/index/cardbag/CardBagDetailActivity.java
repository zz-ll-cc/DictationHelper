package cn.edu.hebtu.software.listendemo.Mine.index.cardbag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Inventory;
import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.edu.hebtu.software.listendemo.Untils.Constant.USER_KEEP_KEY;


@SuppressLint("SetTextI18n")
public class CardBagDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private LinearLayout llOut;
    private LinearLayout llNoCardDetail;
    private SharedPreferences sp;
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private User user;
    private ImageView back;
    private TextView tvCardAll;
    private TextView tvCardNotUse;
    private TextView tvCardUse;
    private TextView tvCardExpiry;
    private RecyclerView rcCardTagDetail;
    private List<Inventory> inventories;
    private CardBagDetailRecyclerAdapter cardBagDetailRecyclerAdapter;
    private static final int ALL = 0;
    private static final int NOT_USE = 1;
    private static final int USE = 2;
    private static final int EXPIRY = 3;
    private SmartRefreshLayout smart;
    private static final int TYPE_INIT = 101;
    private static final int TYPE_REFRESH = 102;
    private static final int TYPE_REFRESH_FALSE = 104;
    private static final int TYPE_LOADMORE = 103;
    private static final int TYPE_LOADMORE_FALSE = 105;
    private int pageSize = 5;  // 页容量
    private int pageNum = 1;    // 当前页
    private int type = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_INIT:
                    if (msg.obj != null) {
                        Type inventoryType = new TypeToken<List<Inventory>>() {
                        }.getType();
                        inventories = gson.fromJson(msg.obj.toString(), inventoryType);
                        if (inventories.size() == 0) {
                            llNoCardDetail.setVisibility(View.VISIBLE);
                        } else {
                            llNoCardDetail.setVisibility(View.GONE);
                        }
                        inventoriesSeclt(type, inventories);
                    }
                    Log.e("ttttttttInit", inventories.size() + "");
                    break;
                case TYPE_LOADMORE:
                    if (msg.obj != null) {
                        Type inventoryType = new TypeToken<List<Inventory>>() {
                        }.getType();
                        List<Inventory> inventoriesinit = gson.fromJson(msg.obj.toString(), inventoryType);
                        if (inventoriesinit.size() != 0) {
                            pageNum++;
                        } else {
                            Toast.makeText(CardBagDetailActivity.this, "数据加载完毕", Toast.LENGTH_SHORT).show();
                        }
                        if (inventories == null) {
                            inventories = inventoriesinit;
                        } else {
                            inventories.addAll(inventoriesinit);
                        }
                        if (inventories.size() == 0) {
                            llNoCardDetail.setVisibility(View.VISIBLE);
                        } else {
                            llNoCardDetail.setVisibility(View.GONE);
                        }
                        inventoriesSeclt(type, inventories);
                    }
                    Log.e("ttttttttLoadMore", inventories.size() + "");
                    smart.finishLoadMore();
                    break;
                case TYPE_LOADMORE_FALSE:
                    Toast.makeText(CardBagDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    smart.finishLoadMore();
                    break;
                case TYPE_REFRESH:
                    if (msg.obj != null) {
                        Type inventoryType = new TypeToken<List<Inventory>>() {
                        }.getType();
                        inventories = gson.fromJson(msg.obj.toString(), inventoryType);
                        if (inventories.size() == 0) {
                            llNoCardDetail.setVisibility(View.VISIBLE);
                        } else {
                            llNoCardDetail.setVisibility(View.GONE);
                        }
                        inventoriesSeclt(type, inventories);

                    }
                    Log.e("ttttttttrefresh", inventories.size() + "");
                    smart.finishRefresh();
                    break;
                case TYPE_REFRESH_FALSE:
                    Toast.makeText(CardBagDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    smart.finishRefresh();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_bag_detail);
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.white);
        marginTopStateBar();
        findView();
//        initData();
        context = this;
        sp = getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        user = gson.fromJson(sp.getString(USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        List<UnLock> unLocks = null;
        try {
            JSONObject jsonObject = new JSONObject(sp.getString(Constant.USER_KEEP_KEY, ""));
            String unLockList = jsonObject.get("unlockList").toString();
            Type type = new TypeToken<List<UnLock>>() {
            }.getType();
            unLocks = new Gson().fromJson(unLockList, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user.setUnLockList(unLocks);
        getMyInventory(user.getUid(), true, TYPE_INIT);
        setListeners();
    }

    private void initData() {
        Intent intent = getIntent();
        String str = intent.getStringExtra(Constant.CARD_BAG_DETAIL);
        if (str != null && !str.equals("")) {
            Type type = new TypeToken<List<Inventory>>() {
            }.getType();
            inventories = gson.fromJson(str + "", type);
        }
        if (inventories.size() == 0) {
            llNoCardDetail.setVisibility(View.VISIBLE);
        } else {
            llNoCardDetail.setVisibility(View.GONE);
        }
        cardBagDetailRecyclerAdapter = new CardBagDetailRecyclerAdapter(user,inventories, CardBagDetailActivity.this, R.layout.activity_card_bag_detail_recycler_item);
        rcCardTagDetail.setAdapter(cardBagDetailRecyclerAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CardBagDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        rcCardTagDetail.setLayoutManager(layoutManager);//必须调用，设置布局管理器
    }

    private void marginTopStateBar() {
        llOut = findViewById(R.id.ll_syllabus_out);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = StatusBarUtil.getStatusBarHeight(this);
        llOut.setLayoutParams(layoutParams);
    }

    public void findView() {
        back = findViewById(R.id.iv_credit_bag_detail_exit);
        back.setOnClickListener(this);
        tvCardAll = findViewById(R.id.tv_card_all);
        tvCardAll.setOnClickListener(this);
        tvCardNotUse = findViewById(R.id.tv_card_not_use);
        tvCardNotUse.setOnClickListener(this);
        tvCardUse = findViewById(R.id.tv_card_use);
        tvCardUse.setOnClickListener(this);
        tvCardExpiry = findViewById(R.id.tv_card_expiry);
        tvCardExpiry.setOnClickListener(this);
        rcCardTagDetail = findViewById(R.id.rc_card_tag_detail);
        llNoCardDetail = findViewById(R.id.ll_no_card_detail);
        smart = findViewById(R.id.smart_detail_card);
        smart.setRefreshHeader(new BezierRadarHeader(this));
        smart.setRefreshFooter(new BallPulseFooter(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.white);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_credit_bag_detail_exit:
                finish();
                break;
            case R.id.tv_card_all:
                tvCardAll.setTextColor(Color.parseColor("#ff5511"));
                tvCardUse.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardNotUse.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardExpiry.setTextColor(Color.parseColor("#c9c9c9"));
                type = ALL;
                getMyInventory(user.getUid(), true, TYPE_INIT);
                break;
            case R.id.tv_card_not_use:
                tvCardNotUse.setTextColor(Color.parseColor("#ff5511"));
                tvCardUse.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardAll.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardExpiry.setTextColor(Color.parseColor("#c9c9c9"));
                type = NOT_USE;
                getMyInventory(user.getUid(), true, TYPE_INIT);
                break;
            case R.id.tv_card_use:
                tvCardUse.setTextColor(Color.parseColor("#ff5511"));
                tvCardAll.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardNotUse.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardExpiry.setTextColor(Color.parseColor("#c9c9c9"));
                type = USE;
                getMyInventory(user.getUid(), true, TYPE_INIT);
                break;
            case R.id.tv_card_expiry:
                tvCardExpiry.setTextColor(Color.parseColor("#ff5511"));
                tvCardUse.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardNotUse.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardAll.setTextColor(Color.parseColor("#c9c9c9"));
                type = EXPIRY;
                getMyInventory(user.getUid(), true, TYPE_INIT);
                break;

        }
    }

    private void setListeners() {
        smart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getMyInventory(user.getUid(), false, TYPE_LOADMORE);
            }
        });
        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getMyInventory(user.getUid(), true, TYPE_REFRESH);
            }
        });
    }

    //获取我的优惠卷
    private void getMyInventory(int userId, boolean isRefresh, int type) {
        if (isRefresh) {
            pageNum = 1;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = null;
        if (type == TYPE_LOADMORE) {
            fb = new FormBody.Builder().add("userId", userId + "")
                    .add("pageSize", pageSize + "")
                    .add("pageNum", pageNum + 1 + "").build();
        } else {
            fb = new FormBody.Builder().add("userId", userId + "")
                    .add("pageSize", pageSize + "")
                    .add("pageNum", pageNum + "").build();
        }
        Log.e("ttttttttpageNum", pageNum + "");
        Request request = new Request.Builder().url(Constant.URL_GET_MY_INVENTORY).post(fb).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                if (type == TYPE_LOADMORE) {
                    message.what = TYPE_LOADMORE_FALSE;
                    handler.sendMessage(message);
                }
                if (type == TYPE_REFRESH) {
                    message.what = TYPE_REFRESH_FALSE;
                    handler.sendMessage(message);
                }
                e.printStackTrace();
            }

            /**
             * 未完待续
             *
             * @param call
             * @param response
             * @throws IOException
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("ttttttttMyInventoryjson", json);
                Message message = new Message();
                message.what = type;
                message.obj = json;
                handler.sendMessage(message);
            }
        });
    }

    public void inventoriesSeclt(int type, List<Inventory> inventories) {
        switch (type) {
            case ALL:
                if (inventories != null) {
                    Log.e("tttttttt全部", inventories.toString());
                    if (cardBagDetailRecyclerAdapter == null) {
                        cardBagDetailRecyclerAdapter = new CardBagDetailRecyclerAdapter(user,inventories, CardBagDetailActivity.this, R.layout.activity_card_bag_detail_recycler_item);
                        rcCardTagDetail.setAdapter(cardBagDetailRecyclerAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CardBagDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                        rcCardTagDetail.setLayoutManager(layoutManager);//必须调用，设置布局管理器
                    } else {
                        cardBagDetailRecyclerAdapter.changeDataSource(inventories);
                    }
                }
                break;
            case NOT_USE:
                List<Inventory> inventories3 = new ArrayList<>();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time1 = format1.format(Calendar.getInstance().getTime());
                try {
                    Date nowDate = format1.parse(time1);
                    System.out.println("完整的时间和日期： " + time1);
                    if (inventories != null) {
                        for (int i = 0; i < inventories.size(); i++) {
                            String expendTime = inventories.get(i).getExpiryTime();
                            String[] arr = expendTime.split("T");
                            String[] tarr = arr[1].split(".000");
                            String dd = arr[0] + " " + tarr[0];
                            Date expiryDate = format1.parse(dd);
                            if (inventories.get(i).getIsUsed() == 0 && nowDate.getTime() < expiryDate.getTime()) {
                                inventories3.add(inventories.get(i));
                            }
                        }
                        Log.e("tttttttt未使用", inventories3.toString());
                        if (cardBagDetailRecyclerAdapter == null) {
                            cardBagDetailRecyclerAdapter = new CardBagDetailRecyclerAdapter(user,inventories3, CardBagDetailActivity.this, R.layout.activity_card_bag_detail_recycler_item);
                            rcCardTagDetail.setAdapter(cardBagDetailRecyclerAdapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CardBagDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                            rcCardTagDetail.setLayoutManager(layoutManager);//必须调用，设置布局管理器
                        } else {
                            cardBagDetailRecyclerAdapter.changeDataSource(inventories3);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case USE:
                List<Inventory> inventories2 = new ArrayList<>();
                if (inventories != null) {
                    for (int i = 0; i < inventories.size(); i++) {
                        if (inventories.get(i).getIsUsed() == 1) {
                            inventories2.add(inventories.get(i));
                        }
                    }
                    Log.e("tttttttt使用", inventories2.toString());
                    if (cardBagDetailRecyclerAdapter == null) {
                        cardBagDetailRecyclerAdapter = new CardBagDetailRecyclerAdapter(user,inventories2, CardBagDetailActivity.this, R.layout.activity_card_bag_detail_recycler_item);
                        rcCardTagDetail.setAdapter(cardBagDetailRecyclerAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CardBagDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                        rcCardTagDetail.setLayoutManager(layoutManager);//必须调用，设置布局管理器
                    } else {
                        cardBagDetailRecyclerAdapter.changeDataSource(inventories2);
                    }
                }
                break;
            case EXPIRY:
                List<Inventory> inventories1 = new ArrayList<>();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = format.format(Calendar.getInstance().getTime());
                try {
                    Date nowDate = format.parse(time);
                    Log.e("完整的时间和日期 ", time);
                    Log.e("完整的时间和日期 ", inventories.size() + "");
                    if (inventories != null) {
                        for (int i = 0; i < inventories.size(); i++) {
                            Log.e("完整的时间和日期 ", inventories.get(i).getExpiryTime());
                            String expendTime = inventories.get(i).getExpiryTime();
                            String[] arr = expendTime.split("T");
                            String[] tarr = arr[1].split(".000");
                            String dd = arr[0] + " " + tarr[0];
                            Date expiryDate = format.parse(dd);
                            Log.e("完整的时间和日期 ", nowDate.getTime() + "  " + expiryDate.getTime());
                            if (inventories.get(i).getIsUsed() == 0 && nowDate.getTime() > expiryDate.getTime()) {
                                inventories1.add(inventories.get(i));
                            }
                        }
                        if (cardBagDetailRecyclerAdapter == null) {
                            cardBagDetailRecyclerAdapter = new CardBagDetailRecyclerAdapter(user,inventories1, CardBagDetailActivity.this, R.layout.activity_card_bag_detail_recycler_item);
                            rcCardTagDetail.setAdapter(cardBagDetailRecyclerAdapter);
                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CardBagDetailActivity.this, LinearLayoutManager.VERTICAL, false);
                            rcCardTagDetail.setLayoutManager(layoutManager);//必须调用，设置布局管理器
                        } else {
                            cardBagDetailRecyclerAdapter.changeDataSource(inventories1);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }


}

