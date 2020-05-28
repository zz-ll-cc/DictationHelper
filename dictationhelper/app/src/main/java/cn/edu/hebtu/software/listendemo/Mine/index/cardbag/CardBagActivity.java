package cn.edu.hebtu.software.listendemo.Mine.index.cardbag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Inventory;
import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Mine.index.credit.CreditDetailActivity;
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
public class CardBagActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private LinearLayout llOut;
    private SharedPreferences sp;
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private User user;
    private ImageView back;
    private TextView tvSum;
    private LinearLayout llCardTagDetail;
    private LinearLayout llNoCard;
    private RecyclerView rcCardTag;
    private CardBagMineRecyclerAdapter cardBagMineRecyclerAdapter;
    private EditText etCardBagChange;
    private TextView tvCardBagChange;
    private List<Inventory> inventories;
    private SmartRefreshLayout smart;
    private static final int EXCHANGE_CODE=1000;
    private static final int TYPE_INIT = 101;
    private static final int TYPE_REFRESH = 102;
    private static final int TYPE_REFRESH_FALSE = 104;
    private static final int TYPE_LOADMORE = 103;
    private static final int TYPE_LOADMORE_FALSE = 105;
    private int pageSize = 5;  // 页容量
    private int pageNum = 1;    // 当前页
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_INIT:
                    if (msg.obj != null) {
                        Type inventoryType = new TypeToken<List<Inventory>>() {
                        }.getType();
                        List<Inventory> inventoriesinit = gson.fromJson(msg.obj.toString(), inventoryType);
                        inventories = new ArrayList<>();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = format.format(Calendar.getInstance().getTime());
                        try {
                            Date nowDate = format.parse(time);
                            inventories.clear();
                            for (int i = 0; i < inventoriesinit.size(); i++) {
                                String expendTime = inventoriesinit.get(i).getExpiryTime();
                                String[] arr = expendTime.split("T");
                                String[] tarr = arr[1].split(".000");
                                String dd = arr[0] + " " + tarr[0];
                                Date expiryDate = format.parse(dd);
                                if (inventoriesinit.get(i).getIsUsed() == 0 && nowDate.getTime() < expiryDate.getTime()) {
                                    inventories.add(inventoriesinit.get(i));
                                }
                            }
                            tvSum.setText(inventories.size() + "");
                            if (inventories.size() == 0) {
                                llNoCard.setVisibility(View.VISIBLE);
                            } else {
                                llNoCard.setVisibility(View.GONE);
                            }
                            if (cardBagMineRecyclerAdapter == null) {
                                cardBagMineRecyclerAdapter = new CardBagMineRecyclerAdapter(user, inventories, CardBagActivity.this, R.layout.activity_card_bag_mine_recycler_item);
                                rcCardTag.setAdapter(cardBagMineRecyclerAdapter);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CardBagActivity.this, LinearLayoutManager.VERTICAL, false);
                                rcCardTag.setLayoutManager(layoutManager);//必须调用，设置布局管理器
                            } else {
                                cardBagMineRecyclerAdapter.notifyDataSetChanged();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case TYPE_LOADMORE:
                    if (msg.obj != null) {
                        Type inventoryType = new TypeToken<List<Inventory>>() {
                        }.getType();
                        List<Inventory> inventoriesinit = gson.fromJson(msg.obj.toString(), inventoryType);
                        if (inventoriesinit.size() != 0) {
                            pageNum++;
                        } else {
                            Toast.makeText(CardBagActivity.this, "数据加载完毕", Toast.LENGTH_SHORT).show();
                        }
                        List<Inventory> inventoriesinit2 = new ArrayList<>();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = format.format(Calendar.getInstance().getTime());
                        try {
                            Date nowDate = format.parse(time);
                            for (int i = 0; i < inventoriesinit.size(); i++) {
                                String expendTime = inventoriesinit.get(i).getExpiryTime();
                                String[] arr = expendTime.split("T");
                                String[] tarr = arr[1].split(".000");
                                String dd = arr[0] + " " + tarr[0];
                                Date expiryDate = format.parse(dd);
                                if (inventoriesinit.get(i).getIsUsed() == 0 && nowDate.getTime() < expiryDate.getTime()) {
                                    inventoriesinit2.add(inventoriesinit.get(i));
                                }
                            }
                            if (inventories == null) {
                                inventories = inventoriesinit2;
                            } else {
                                inventories.addAll(inventoriesinit2);
                            }
                            tvSum.setText(inventories.size() + "");
                            if (inventories.size() == 0) {
                                llNoCard.setVisibility(View.VISIBLE);
                            } else {
                                llNoCard.setVisibility(View.GONE);
                            }
                            if (cardBagMineRecyclerAdapter == null) {
                                cardBagMineRecyclerAdapter = new CardBagMineRecyclerAdapter(user, inventories, CardBagActivity.this, R.layout.activity_card_bag_mine_recycler_item);
                                rcCardTag.setAdapter(cardBagMineRecyclerAdapter);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CardBagActivity.this, LinearLayoutManager.VERTICAL, false);
                                rcCardTag.setLayoutManager(layoutManager);//必须调用，设置布局管理器
                            } else {
                                cardBagMineRecyclerAdapter.notifyDataSetChanged();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    Log.e("ttttttttLoadMore", inventories.size() + "");
                    smart.finishLoadMore();
                    break;
                case TYPE_LOADMORE_FALSE:
                    Toast.makeText(CardBagActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    smart.finishLoadMore();
                    break;
                case TYPE_REFRESH:
                    if (msg.obj != null) {
                        Type inventoryType = new TypeToken<List<Inventory>>() {
                        }.getType();
                        List<Inventory> inventoriesinit = gson.fromJson(msg.obj.toString(), inventoryType);
                        inventories = new ArrayList<>();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = format.format(Calendar.getInstance().getTime());
                        try {
                            Date nowDate = format.parse(time);
                            inventories.clear();
                            for (int i = 0; i < inventoriesinit.size(); i++) {
                                String expendTime = inventoriesinit.get(i).getExpiryTime();
                                String[] arr = expendTime.split("T");
                                String[] tarr = arr[1].split(".000");
                                String dd = arr[0] + " " + tarr[0];
                                Date expiryDate = format.parse(dd);
                                if (inventoriesinit.get(i).getIsUsed() == 0 && nowDate.getTime() < expiryDate.getTime()) {
                                    inventories.add(inventoriesinit.get(i));
                                }
                            }
                            tvSum.setText(inventories.size() + "");
                            if (inventories.size() == 0) {
                                llNoCard.setVisibility(View.VISIBLE);
                            } else {
                                llNoCard.setVisibility(View.GONE);
                            }
                            if (cardBagMineRecyclerAdapter == null) {
                                cardBagMineRecyclerAdapter = new CardBagMineRecyclerAdapter(user, inventories, CardBagActivity.this, R.layout.activity_card_bag_mine_recycler_item);
                                rcCardTag.setAdapter(cardBagMineRecyclerAdapter);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CardBagActivity.this, LinearLayoutManager.VERTICAL, false);
                                rcCardTag.setLayoutManager(layoutManager);//必须调用，设置布局管理器
                            } else {
                                cardBagMineRecyclerAdapter.notifyDataSetChanged();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    Log.e("ttttttttrefresh", inventories.size() + "");
                    smart.finishRefresh();
                    break;
                case TYPE_REFRESH_FALSE:
                    Toast.makeText(CardBagActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    smart.finishRefresh();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_bag);
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.white);
        EventBus.getDefault().register(this);
        marginTopStateBar();
        context = this;
        findView();
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

    private void marginTopStateBar() {
        llOut = findViewById(R.id.ll_syllabus_out);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = StatusBarUtil.getStatusBarHeight(this);
        llOut.setLayoutParams(layoutParams);
    }

    public void findView() {
        back = findViewById(R.id.iv_credit_bag_exit);
        back.setOnClickListener(this);
        tvSum = findViewById(R.id.tv_card_tag_sum);
        llCardTagDetail = findViewById(R.id.ll_card_tag_detail);
        llCardTagDetail.setOnClickListener(this);
        rcCardTag = findViewById(R.id.rc_card_tag);
        etCardBagChange = findViewById(R.id.et_card_bag_change);
        tvCardBagChange = findViewById(R.id.tv_card_bag_change);
        tvCardBagChange.setOnClickListener(this);
        llNoCard = findViewById(R.id.ll_no_card);
        smart = findViewById(R.id.smart_mine_card);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUnLockMsg(Map<String, Object> postMap) {
        switch (postMap.get("type").toString()) {
            case "cardbaguse":
                String result = postMap.get("result").toString();
                String inventoryId = postMap.get("inventoryId").toString();
                Log.e("EventBus", "cardbaguse" + "_" + inventoryId);
                Inventory inventory = gson.fromJson(result + "", Inventory.class);
                Toast.makeText(this, "卡卷成功使用！", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < inventories.size(); i++) {
                    if (inventoryId.equals(inventories.get(i).getId() + "")) {
                        inventories.remove(i);
                    }
                }
                if (inventories.size() == 0) {
                    llNoCard.setVisibility(View.VISIBLE);
                } else {
                    llNoCard.setVisibility(View.GONE);
                }
                cardBagMineRecyclerAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_credit_bag_exit:
                finish();
                break;
            case R.id.ll_card_tag_detail:
                Intent intent = new Intent(CardBagActivity.this, CardBagDetailActivity.class);
                intent.putExtra(Constant.CARD_BAG_DETAIL, gson.toJson(inventories));
                startActivity(intent);
                break;
            case R.id.tv_card_bag_change:
                String code = etCardBagChange.getText().toString();
                exchangeCode(1,code,user.getUid());
                Toast.makeText(context, etCardBagChange.getText() + "  " + "兑换", Toast.LENGTH_SHORT).show();
                break;
        }
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

    //获取
    private void exchangeCode(int typeId, String redeemString, int userId) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("userId", userId + "")
                    .add("isRefresh", redeemString + "")
                    .add("type", typeId + "").build();
        Request request = new Request.Builder().url(Constant.URL_GET_MY_INVENTORY).post(fb).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
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
                message.what =EXCHANGE_CODE ;
                message.obj = json;
                handler.sendMessage(message);
            }
        });
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

}

