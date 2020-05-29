package cn.edu.hebtu.software.listendemo.Mine.index.credit;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.CreditRecord;
import cn.edu.hebtu.software.listendemo.Entity.MonthCreditRecord;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CreditDetailActivity extends AppCompatActivity implements View.OnClickListener {
    // 控件
    private ImageView ivExit;
    private RecyclerView rcvOut;
    private SmartRefreshLayout smart;
    private LinearLayout llOut;
    // 数据源
    private List<CreditRecord> creditRecordList = new ArrayList<>();        // 网络获取
    private List<MonthCreditRecord> monthCreditRecords = new ArrayList<>(); // 一层适配
    // 初始数据
    private int userId;
    private int pageSize = 10;  // 页容量
    private int pageNum = 0;    // 当前页
    //    private static final int GET_PAGE = 100;
    private static final int TYPE_INIT = 101;
    private static final int TYPE_REFRESH = 102;
    private static final int TYPE_REFRESH_FALSE = 104;
    private static final int TYPE_LOADMORE = 103;
    private static final int TYPE_LOADMORE_FALSE = 105;


    // 工具
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    // 适配器
    private CreditDetailRecyclerOutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_detail);
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.green);
        initDatas();
        findViews();
        smart.setRefreshHeader(new MaterialHeader(this));
        smart.setRefreshFooter(new BallPulseFooter(this));
        rcvOut.addItemDecoration(new SpacesItemDecoration(10));
        marginTopStateBar();
        setListeners();
        getCreditDetailDatas(true, TYPE_INIT);
    }

    private void marginTopStateBar() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.topMargin = StatusBarUtil.getStatusBarHeight(this);
        llOut.setLayoutParams(layoutParams);
    }

    private void setListeners() {
        ivExit.setOnClickListener(this::onClick);
        smart.setOnRefreshListener(new OnRefreshListener() {    // 下拉刷新
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getCreditDetailDatas(true, TYPE_REFRESH);
            }
        });
        smart.setOnLoadMoreListener(new OnLoadMoreListener() {  // 上拉加载更多
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getCreditDetailDatas(false, TYPE_LOADMORE);
            }
        });
    }

    private void getCreditDetailDatas(boolean isRefresh, int type) {
        if (isRefresh) {
            pageNum = 0;
        }
        FormBody.Builder builder = new FormBody.Builder().add("pageSize", pageSize + "")
                .add("userId", userId + "")
                .add("currentPage", ++pageNum + "");
        Log.e("nowPageNum",pageNum+"");
        FormBody fb = builder.build();
        Request request = new Request.Builder()
                .url(Constant.URL_GET_CREDIT_DETAIL)
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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TYPE_INIT:
                    Type type = new TypeToken<List<CreditRecord>>() {
                    }.getType();
                    if (pageNum == 1) {
                        creditRecordList = gson.fromJson(msg.obj.toString(), type);
                        initData2MonthList();
                        if (adapter == null) {
                            adapter = new CreditDetailRecyclerOutAdapter(monthCreditRecords, CreditDetailActivity.this, R.layout.activity_credit_detail_out_item);
                            rcvOut.setAdapter(adapter);
                        } else {
                            adapter.changeDataSource(monthCreditRecords);
                        }
                    } else {
                        List<CreditRecord> add = gson.fromJson(msg.obj.toString(), type);
                        creditRecordList.addAll(add);
                        initData2MonthList();
                        adapter.changeDataSource(monthCreditRecords);
                    }
                    break;
                case TYPE_LOADMORE:
                    Type type1 = new TypeToken<List<CreditRecord>>() {
                    }.getType();
                    if (pageNum == 1) {
                        creditRecordList = gson.fromJson(msg.obj.toString(), type1);
                        initData2MonthList();
                        if (adapter == null) {
                            adapter = new CreditDetailRecyclerOutAdapter(monthCreditRecords, CreditDetailActivity.this, R.layout.activity_credit_detail_out_item);
                            rcvOut.setAdapter(adapter);
                        } else {
                            adapter.changeDataSource(monthCreditRecords);
                        }
                    } else {
                        List<CreditRecord> add = gson.fromJson(msg.obj.toString(), type1);
                        creditRecordList.addAll(add);
                        initData2MonthList();
                        adapter.changeDataSource(monthCreditRecords);
                    }
                    Log.e("creditRecordLoadMore",creditRecordList.size()+"");
                    smart.finishLoadMore();
                    break;
                case TYPE_REFRESH:
                    Type type2 = new TypeToken<List<CreditRecord>>() {
                    }.getType();
                    creditRecordList = gson.fromJson(msg.obj.toString(), type2);
                    Log.e("creditRecordRefresh",creditRecordList.size()+"");
                    initData2MonthList();
                    if (adapter == null) {
                        adapter = new CreditDetailRecyclerOutAdapter(monthCreditRecords, CreditDetailActivity.this, R.layout.activity_credit_detail_out_item);
                        rcvOut.setAdapter(adapter);
                    } else {
                        adapter.changeDataSource(monthCreditRecords);
                    }
                    smart.finishRefresh();
                    break;
                case TYPE_LOADMORE_FALSE:
                    Toast.makeText(CreditDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    smart.finishLoadMore();
                    break;
                case TYPE_REFRESH_FALSE:
                    Toast.makeText(CreditDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    smart.finishRefresh();
                    break;
            }
        }
    };

    /**
     * 讲前端获取的数据放到按month获取的里边
     */
    private void initData2MonthList() {
        monthCreditRecords.clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        for (CreditRecord creditRecord : creditRecordList) {
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(creditRecord.getCreateTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (monthCreditRecords.size() == 0) {
                MonthCreditRecord monthCreditRecord = new MonthCreditRecord();
                monthCreditRecord.setRecordTime(sdf.format(date));
                List<CreditRecord> creditRecords = new ArrayList<>();
                creditRecords.add(creditRecord);
                monthCreditRecord.setCreditRecords(creditRecords);
                monthCreditRecords.add(monthCreditRecord);
            } else {
                boolean haveThis = false;
                for (int i = 0; i < monthCreditRecords.size(); i++) {
                    MonthCreditRecord monthCreditRecord = monthCreditRecords.get(i);
                    if (monthCreditRecord.getRecordTime().equals(sdf.format(date))) {
                        monthCreditRecord.getCreditRecords().add(creditRecord);
                        haveThis = true;
                        break;
                    }
                }
                if (!haveThis) {
                    MonthCreditRecord monthCreditRecord = new MonthCreditRecord();
                    monthCreditRecord.setRecordTime(sdf.format(date));
                    List<CreditRecord> creditRecords = new ArrayList<>();
                    creditRecords.add(creditRecord);
                    monthCreditRecord.setCreditRecords(creditRecords);
                    monthCreditRecords.add(monthCreditRecord);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtil.statusBarLightMode(this);
        StatusBarUtil.setStatusBarColor(this, R.color.green);
    }

    private void findViews() {
        ivExit = findViewById(R.id.iv_credit_detail_exit);
        rcvOut = findViewById(R.id.rcv_credit_detail_item_out);
        smart = findViewById(R.id.smart_credit_detail);
        llOut = findViewById(R.id.ll_credit_detail_out);
    }

    private void initDatas() {
        userId = getIntent().getIntExtra("userId", -1);
//        userId = 1;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_credit_detail_exit:
                finish();
                break;
        }
    }
}
