package cn.edu.hebtu.software.listendemo.Mine.index.cardbag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import cn.edu.hebtu.software.listendemo.Entity.Item;
import cn.edu.hebtu.software.listendemo.Entity.Record;
import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Mine.index.credit.CreditDetailActivity;
import cn.edu.hebtu.software.listendemo.Mine.index.credit.SignActivity;
import cn.edu.hebtu.software.listendemo.QiniuUtils.Json;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import cn.edu.hebtu.software.listendemo.credit.component.CalendarDate;
import cn.edu.hebtu.software.listendemo.credit.component.MonthPager;
import cn.edu.hebtu.software.listendemo.credit.interf.OnSelectDateListener;
import cn.edu.hebtu.software.listendemo.credit.task.TaskAdapter;
import cn.edu.hebtu.software.listendemo.credit.view.CalendarViewAdapter;
import cn.edu.hebtu.software.listendemo.credit.view.ThemeDayView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.edu.hebtu.software.listendemo.Untils.Constant.URL_GET_ITEM;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.USER_KEEP_KEY;


@SuppressLint("SetTextI18n")
public class CreditBagActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private LinearLayout llOut;
    private SharedPreferences sp;
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private User user;
    private ImageView back;
    private TextView tvSum;
    private LinearLayout llCardTagDetail;
    private RecyclerView rcCardTag;
    private EditText etCardBagChange;
    private TextView tvCardBagChange;
    private List<Inventory> inventories;
    private static final int GET_ITEM = 100;
    private static final int GET_MY_INVENTORY = 200;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_MY_INVENTORY:
                    if (msg.obj != null) {
                        Type inventoryType = new TypeToken<List<Inventory>>() {
                        }.getType();
                        inventories = gson.fromJson(msg.obj + "", inventoryType);
                        List<Inventory> inventories1=new ArrayList<>();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String time = format.format(Calendar.getInstance().getTime());
                        try {
                            Date nowDate = format.parse(time);
                            for (int i = 0; i < inventories.size(); i++) {
                                String expendTime = inventories.get(i).getExpiryTime();
                                String[] arr = expendTime.split("T");
                                String[] tarr = arr[1].split(".000");
                                String dd = arr[0]+" "+tarr[0];
                                Date expiryDate = format.parse(dd);
                                if (inventories.get(i).getIsUsed() == 0 && nowDate.getTime()<expiryDate.getTime()) {
                                    inventories1.add(inventories.get(i));
                                }
                            }
                            tvSum.setText(inventories1.size()+"");
                            CreditBagMineRecyclerAdapter creditBagMineRecyclerAdapter = new CreditBagMineRecyclerAdapter(inventories1, CreditBagActivity.this, R.layout.activity_card_bag_mine_recycler_item);
                            rcCardTag.setAdapter(creditBagMineRecyclerAdapter);
                            RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(CreditBagActivity.this, LinearLayoutManager.VERTICAL,false);
                            rcCardTag.setLayoutManager(layoutManager);//必须调用，设置布局管理器
//                       rcCardTag.setLayoutManager(new GridLayoutManager(CreditBagActivity.this, 3));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
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

        //getItems(user.getUid());
        getMyInventory(user.getUid());
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
            case R.id.iv_credit_bag_exit:
                finish();
                break;
            case R.id.ll_card_tag_detail:
                Intent intent=new Intent(CreditBagActivity.this,CreditBagDetailActivity.class);
                intent.putExtra(Constant.CARD_BAG_DETAIL,gson.toJson(inventories));
                startActivity(intent);
                break;
            case R.id.tv_card_bag_change:
                Toast.makeText(context, etCardBagChange.getText()+"  "+"兑换", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //获取我的优惠卷
    private void getMyInventory(int userId) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("userId", userId + "")
                .add("pageSize",9999+"").build();
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
                Log.e("MyInventoryjson", json);
                Message message = new Message();
                message.what = GET_MY_INVENTORY;
                message.obj = json;
                handler.sendMessage(message);
            }
        });
    }

}

