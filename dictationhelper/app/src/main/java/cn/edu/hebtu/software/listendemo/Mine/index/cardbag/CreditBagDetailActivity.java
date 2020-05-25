package cn.edu.hebtu.software.listendemo.Mine.index.cardbag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
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
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Inventory;
import cn.edu.hebtu.software.listendemo.Entity.Item;
import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Entity.Word;
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
public class CreditBagDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private LinearLayout llOut;
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
    private List<Inventory> inventories1 = new ArrayList<>();
    private CreditBagDetailRecyclerAdapter creditBagDetailRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_bag_detail);
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.white);
        marginTopStateBar();
        findView();
        initData();
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

    }

    private void initData() {
        Intent intent = getIntent();
        String str = intent.getStringExtra(Constant.CARD_BAG_DETAIL);
        if (str != null && !str.equals("")) {
            Type type = new TypeToken<List<Inventory>>() {
            }.getType();
            inventories = gson.fromJson(str + "", type);
        }
        creditBagDetailRecyclerAdapter = new CreditBagDetailRecyclerAdapter(inventories, CreditBagDetailActivity.this, R.layout.activity_card_bag_detail_recycler_item);
        rcCardTagDetail.setAdapter(creditBagDetailRecyclerAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CreditBagDetailActivity.this, LinearLayoutManager.VERTICAL, false);
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
                inventories1.clear();
                for (int i = 0; i < inventories.size(); i++) {
                    inventories1.add(inventories.get(i));
                }
                creditBagDetailRecyclerAdapter.changeDataSource(inventories1);
                break;
            case R.id.tv_card_not_use:
                tvCardNotUse.setTextColor(Color.parseColor("#ff5511"));
                tvCardUse.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardAll.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardExpiry.setTextColor(Color.parseColor("#c9c9c9"));
                inventories1.clear();
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time1 = format1.format(Calendar.getInstance().getTime());
                try {
                    Date nowDate = format1.parse(time1);
                    System.out.println("完整的时间和日期： " + time1);
                    for (int i = 0; i < inventories.size(); i++) {
                        String expendTime = inventories.get(i).getExpiryTime();
                        String[] arr = expendTime.split("T");
                        String[] tarr = arr[1].split(".000");
                        String dd = arr[0]+" "+tarr[0];
                        Date expiryDate = format1.parse(dd);
                        if (inventories.get(i).getIsUsed() == 0 && nowDate.getTime()<expiryDate.getTime()) {
                            inventories1.add(inventories.get(i));
                        }
                    }
                    creditBagDetailRecyclerAdapter.changeDataSource(inventories1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_card_use:
                tvCardUse.setTextColor(Color.parseColor("#ff5511"));
                tvCardAll.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardNotUse.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardExpiry.setTextColor(Color.parseColor("#c9c9c9"));
                inventories1.clear();
                for (int i = 0; i < inventories.size(); i++) {
                    if (inventories.get(i).getIsUsed() == 1) {
                        inventories1.add(inventories.get(i));
                    }
                }
                creditBagDetailRecyclerAdapter.changeDataSource(inventories1);
                break;
            case R.id.tv_card_expiry:
                tvCardExpiry.setTextColor(Color.parseColor("#ff5511"));
                tvCardUse.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardNotUse.setTextColor(Color.parseColor("#c9c9c9"));
                tvCardAll.setTextColor(Color.parseColor("#c9c9c9"));
                inventories1.clear();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String time = format.format(Calendar.getInstance().getTime());
                try {
                    Date nowDate = format.parse(time);
                    System.out.println("完整的时间和日期： " + time);
                    for (int i = 0; i < inventories.size(); i++) {
                        String expendTime = inventories.get(i).getExpiryTime();
                        String[] arr = expendTime.split("T");
                        String[] tarr = arr[1].split(".000");
                        String dd = arr[0]+" "+tarr[0];
                        Date expiryDate = format.parse(dd);
                        if (inventories.get(i).getIsUsed() == 0 && nowDate.getTime()>expiryDate.getTime()) {
                            inventories1.add(inventories.get(i));
                        }
                    }
                    creditBagDetailRecyclerAdapter.changeDataSource(inventories1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


}

