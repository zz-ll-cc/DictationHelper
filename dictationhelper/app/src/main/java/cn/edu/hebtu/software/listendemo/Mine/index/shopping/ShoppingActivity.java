package cn.edu.hebtu.software.listendemo.Mine.index.shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
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
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Item;
import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Mine.index.cardbag.CardBagActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.BlurTransformation;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.GridSpacingItemDecoration;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.edu.hebtu.software.listendemo.Untils.Constant.SP_NAME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.TYPE_BUY_TYPE_1;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.USER_KEEP_KEY;

public class ShoppingActivity extends AppCompatActivity implements View.OnClickListener {

    // 控件
    private RelativeLayout rlOut;
    private TextView tvCard;
    private RecyclerView rcvShop;
    private SmartRefreshLayout smart;
    // 数据源
    private List<Item> items;
    private User user;
    private List<UnLock> unLocks;
    // 工具
    private SharedPreferences sp;
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();
    private int pageNum = 0;
    private int pageSize = 10;
    private static final int TYPE_INIT = 101;
    private static final int TYPE_REFRESH = 102;
    private static final int TYPE_REFRESH_FALSE = 104;
    private static final int TYPE_LOADMORE = 103;
    private static final int TYPE_LOADMORE_FALSE = 105;
    // 适配器
    private ShopRecyclerAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_INIT:
                    Type type = new TypeToken<List<Item>>() {
                    }.getType();
                    GetLeftTask task = new GetLeftTask(TYPE_INIT);
                    if (pageNum == 1) {
                        items = gson.fromJson(msg.obj.toString(), type);
                    } else {
                        List<Item> add = gson.fromJson(msg.obj.toString(), type);
                        items.addAll(add);
                    }
                    task.execute();
                    break;
                case TYPE_LOADMORE:
                    Type type1 = new TypeToken<List<Item>>() {
                    }.getType();
                    GetLeftTask task2 = new GetLeftTask(TYPE_LOADMORE);
                    if (pageNum == 1) {
                        items = gson.fromJson(msg.obj.toString(), type1);
                    } else {
                        List<Item> add = gson.fromJson(msg.obj.toString(), type1);
                        items.addAll(add);
                    }
                    task2.execute();
                    break;
                case TYPE_REFRESH:
                    Type type2 = new TypeToken<List<Item>>() {
                    }.getType();
                    GetLeftTask task3 = new GetLeftTask(TYPE_REFRESH);
                    items = gson.fromJson(msg.obj.toString(), type2);
                    task3.execute();
                    break;
                case TYPE_LOADMORE_FALSE:
                    Toast.makeText(ShoppingActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    smart.finishLoadMore();
                    break;
                case TYPE_REFRESH_FALSE:
                    Toast.makeText(ShoppingActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    smart.finishRefresh();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        EventBus.getDefault().register(this);
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.bg_shop);
        initDatas();
        findViews();
        marginTopStateBar();
        setListeners();
    }

    class GetLeftTask extends AsyncTask {
        private int type;

        public GetLeftTask(int type) {
            this.type = type;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            FormBody.Builder builder = new FormBody.Builder().add("itemId", items.get(0).getId() + "");
            FormBody fb = builder.build();
            Request request = new Request.Builder()
                    .url(Constant.URL_GET_SHOPPING_ITEM_LEFT_IN_CACHE)
                    .post(fb).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonStr = response.body().string();
                    items.get(0).setLeft(Integer.parseInt(jsonStr));
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            switch (type) {
                case TYPE_INIT:
                    if (adapter == null) {
                        adapter = new ShopRecyclerAdapter(ShoppingActivity.this, items, R.layout.activity_shopping_item, user);
                        rcvShop.setAdapter(adapter);
                    } else {
                        adapter.changeDataSource(items);
                    }
                    getItems(false, TYPE_LOADMORE);
                    break;
                case TYPE_LOADMORE:
                    if (adapter == null) {
                        adapter = new ShopRecyclerAdapter(ShoppingActivity.this, items, R.layout.activity_shopping_item, user);
                        rcvShop.setAdapter(adapter);
                    } else {
                        adapter.changeDataSource(items);
                    }
                    smart.finishLoadMore();
                    break;
                case TYPE_REFRESH:
                    if (adapter == null) {
                        adapter = new ShopRecyclerAdapter(ShoppingActivity.this, items, R.layout.activity_shopping_item, user);
                        rcvShop.setAdapter(adapter);
                    } else {
                        adapter.changeDataSource(items);
                    }
                    getItems(false, TYPE_LOADMORE);
                    smart.finishRefresh();
                    break;
            }
        }
    }

    private void initDatas() {
        sp = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        String userStr = sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER);
        try {
            JSONObject jsonObject = new JSONObject(userStr);
            String unLockList = jsonObject.get("unlockList").toString();
            Type type = new TypeToken<List<UnLock>>() {
            }.getType();
            unLocks = new Gson().fromJson(unLockList, type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user = gson.fromJson(userStr, User.class);
        user.setUnLockList(unLocks);

    }

    private void getItems(boolean isRefresh, int type) {
        if (isRefresh) {
            pageNum = 0;
        }
        FormBody.Builder builder = new FormBody.Builder().add("pageSize", pageSize + "")
                .add("pageNum", ++pageNum + "");
        FormBody fb = builder.build();
        Request request = new Request.Builder()
                .url(Constant.URL_GET_SHOPPING_ITEM)
                .post(fb).build();
        Call call = client.newCall(request);
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
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                Message message = new Message();
                message.what = type;
                message.obj = jsonStr;
                handler.sendMessage(message);
            }
        });
    }

    private void setListeners() {
        tvCard.setOnClickListener(this::onClick);
        smart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getItems(false, TYPE_LOADMORE);
            }
        });
        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getItems(true, TYPE_REFRESH);
            }
        });
    }

    private void findViews() {
        rlOut = findViewById(R.id.rl_my_shop_out);
        tvCard = findViewById(R.id.tv_shopping_to_card);
        rcvShop = findViewById(R.id.rcv_shopping);
        smart = findViewById(R.id.smart_shop);
        // 模糊处理背景
        Glide.with(this)
                .load(R.drawable.bg_shop)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(5, 4)))
                // .apply(RequestOptions.bitmapTransform( new BlurTransformation(context, 20)))
                .into(new ViewTarget<RelativeLayout, Drawable>(rlOut) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Drawable current = resource.getCurrent();
                        rlOut.setBackground(current);
                    }
                });
        smart.setRefreshHeader(new BezierRadarHeader(this));
        smart.setRefreshFooter(new BallPulseFooter(this));
        int spanCount = 2; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = true;
        rcvShop.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
    }

    private void marginTopStateBar() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = StatusBarUtil.getStatusBarHeight(this);
        rlOut.setLayoutParams(layoutParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showBuyResult(Map<String, Object> buyMap) {
        if(buyMap.get("buyType")!=null) {
            String buyType = buyMap.get("buyType").toString();
            if (buyType.equals(TYPE_BUY_TYPE_1)) {
                Item item = (Item) buyMap.get("item");
                // 修改user属性
                user.setUserCredit(user.getUserCredit() - item.getPrice());
                user.setUnLockList(unLocks);
                sp.edit().putString(USER_KEEP_KEY, gson.toJson(user)).commit();
                adapter.updateUser(user);
                // 显示成功的dialog
                ShowBuyAchieveDialog dialog = ShowBuyAchieveDialog.getDialog();
                dialog.setContext(this);
                dialog.setItem(item);
                dialog.setUser(user);
                dialog.showDialog();
            } else {
                Toast.makeText(this, buyType, Toast.LENGTH_SHORT).show();
            }
            getItems(true, TYPE_INIT);     // 获取商品数据
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getItems(true, TYPE_INIT);     // 获取商品数据
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_shopping_to_card:
                // 进入卡券包
                Intent intent = new Intent(this, CardBagActivity.class);
                startActivity(intent);
                break;
        }
    }
}
