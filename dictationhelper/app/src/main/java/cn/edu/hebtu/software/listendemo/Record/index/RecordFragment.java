package cn.edu.hebtu.software.listendemo.Record.index;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.ChartView;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.CorrectSumDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.NewWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;
import cn.edu.hebtu.software.listendemo.Untils.WrongWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.CustomScrollBar;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class RecordFragment extends Fragment {
    private RecyclerView rvShow;
    private RecyclerView rvWord;
    private RecyclerView rvPrecision;
    private ImageView imageView;
    private CustomScrollBar csb;
    //传过来图片的url
    private List<String> urlList;
    private List<Map<String, Object>> showResources = new ArrayList<>();
    private StatisticAdapter adapter;
    private TextView tvWordFive;
    private TextView tvWordMonth;
    private TextView tvPrecisionFive;
    private TextView tvPrecisionMonth;
    private View view;
    private RecordShowAdapter showAdapter;
    private LinearLayout llwordChart;
    private LinearLayout llaccurrencyChart;
    private LinearLayout llwordsearch;
    private LinearLayout llacurrencyseach;
    private List<Map<String, Object>> recordList = new ArrayList<>();
    private SQLiteDatabase currectsumdatabase;
    private  ChartView wordFiveView;
    private  ChartView wordMonthView;
    private  ChartView accrencyFiveView;
    private  ChartView accrencyMonthView;
    public SimpleDateFormat simpleDateFormatt = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            setListener();
            switch (msg.what) {
                case Constant.ACCURENCY_FIVE:
                    List xlist = new ArrayList();
                    List datalist = new ArrayList();
                    for (int i = 0; i < recordList.size(); i++) {
                        String xstr = recordList.get(i).get("date").toString();
                        double accStr= Double.parseDouble(recordList.get(i).get("acc").toString());
                        String accStr1 = (int)accStr+"";
                        Log.e("acc",accStr1);
                        try {
                            Date date = new Date(simpleDateFormatt.parse(xstr).toString());
                            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd");
                            String sDate = simpleDateFormat1.format(date);
                            xlist.add(sDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        datalist.add(accStr1);
                    }
                    String[] strX = (String[]) xlist.toArray(new String[xlist.size()]);
                    String[] strData = (String[]) datalist.toArray(new String[datalist.size()]);
                    Log.e("acc",strX.length+"");
                    if(strX.length!=0 && strData.length!=0){
                        accrencyFiveView.SetInfo(strX, // X轴刻度
                                new String[]{"0", "20", "40", "60", "80", "100"}, // Y轴刻度
                                strData, // 数据
                                "", "百分比");
                        llaccurrencyChart.setBackground(null);
                        llaccurrencyChart.removeAllViews();
                        llaccurrencyChart.addView(accrencyFiveView);
                        llacurrencyseach.setVisibility(View.VISIBLE);
                        /*tvPrecisionFive.setClickable(true);
                        tvPrecisionMonth.setClickable(true);*/
                    }else{
                      llaccurrencyChart.setBackground(getResources().getDrawable(R.drawable.accrencyimg));
                      llacurrencyseach.setVisibility(View.GONE);
                     /* tvPrecisionFive.setClickable(false);
                      tvPrecisionMonth.setClickable(false);*/
                    }
                    break;
            }
//            Bitmap bitmap = (Bitmap)msg.obj;
//            Bitmap bitmap = BitmapFactory.decodeStream()
//            imageView.setImageBitmap(bitmap);//将图片的流转换成图片
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, null, false);
        SharedPreferences sp = getActivity().getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        User user = new Gson().fromJson(sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        Constant.point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(Constant.point);//获取屏幕分辨率
        initView(view);
        initData();
        initAdapter();
        accrencyFiveView = new ChartView(getContext());
        accrencyMonthView = new ChartView(getContext());
        wordFiveView = new ChartView(getContext());
        wordMonthView = new ChartView(getContext());
        setListener();
        getWordFive();
        getAccurencyFiveRecord();
        return view;
    }

    private void setListener() {
        tvWordFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llwordChart.removeAllViews();
                tvWordFive.setTextColor(getResources().getColor(R.color.black));
                tvWordFive.setBackground(getResources().getDrawable(R.drawable.choose_record_img_border_left));
                tvWordMonth.setTextColor(getResources().getColor(R.color.gray));
                tvWordMonth.setBackground(null);
                getWordFive();
            }
        });

        tvWordMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llwordChart.removeAllViews();
                tvWordMonth.setTextColor(getResources().getColor(R.color.black));
                tvWordMonth.setBackground(getResources().getDrawable(R.drawable.choose_record_img_border_right));
                tvWordFive.setTextColor(getResources().getColor(R.color.gray));
                tvWordFive.setBackground(null);
               //getWordMonth();

            }
        });

        tvPrecisionFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPrecisionFive.setTextColor(getResources().getColor(R.color.black));
                tvPrecisionFive.setBackground(getResources().getDrawable(R.drawable.choose_record_img_border_left));
                tvPrecisionMonth.setTextColor(getResources().getColor(R.color.gray));
                tvPrecisionMonth.setBackground(null);
                getAccurencyFiveRecord();
            }
        });

        tvPrecisionMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPrecisionMonth.setTextColor(getResources().getColor(R.color.black));
                tvPrecisionMonth.setBackground(getResources().getDrawable(R.drawable.choose_record_img_border_right));
                tvPrecisionFive.setTextColor(getResources().getColor(R.color.gray));
                tvPrecisionFive.setBackground(null);
                llaccurrencyChart.removeAllViews();
            }
        });


    }

    private void initAdapter() {
//        adapter = new StatisticAdapter(R.layout.fragment_statistics_detail, urlList, getContext());
        showAdapter = new RecordShowAdapter(getContext(), R.layout.fragment_record_recycler_item, showResources);
        rvShow.setAdapter(showAdapter);
        User user = new Gson().fromJson(getActivity().getSharedPreferences(Constant.SP_NAME,MODE_PRIVATE).getString(Constant.USER_KEEP_KEY,Constant.DEFAULT_KEEP_USER),User.class);
        if (user.getUphone().equals("15833143601")){
            csb.setText("已连续学习1天");
    }else {
        csb.setText("已连续学习5天");
    }
    }

    private void initRecycler() {
//        SmoothScrollLayoutManager layoutManager = new SmoothScrollLayoutManager(getContext());
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        rvWord.setAdapter(adapter);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(getActivity(),R.color.backgray);
    }

    @Override
    public void onResume() {
        super.onResume();
        showResources.clear();
        initData();
        showAdapter.notifyDataSetChanged();
        StatusBarUtil.setStatusBarColor(getActivity(),R.color.backgray);
    }

    private void initData() {
        int rawCount = 0;
        int wrongCount = 0;
        NewWordDBHelper newWordDBHelper = new NewWordDBHelper(getContext(), "tbl_newWord.db", 1);
        SQLiteDatabase dbRaw = newWordDBHelper.getWritableDatabase();
        Cursor cursor = dbRaw.query("TBL_NEWWORD", new String[]{"COUNT(*)"},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                rawCount = cursor.getInt(0);
            } while (cursor.moveToNext());
        }
        Map<String, Object> mapNew = new HashMap<>();
        mapNew.put("layoutBackground", R.drawable.note_raw_backborder);
        mapNew.put("content", "生词本");
        mapNew.put("count", rawCount);
        mapNew.put("img", R.drawable.note_raw);
        showResources.add(mapNew);
        WrongWordDBHelper wrongWordDBHelper = new WrongWordDBHelper(getContext(), "tbl_wrongWord.db", 1);
        SQLiteDatabase dbWrong = wrongWordDBHelper.getWritableDatabase();
        Cursor cursor1 = dbWrong.query("TBL_WRONGWORD", new String[]{"COUNT(*)"},
                null, null, null, null, null);
        if (cursor1.moveToFirst()) {
            do {
                wrongCount = cursor1.getInt(0);
            } while (cursor.moveToNext());
        }
        Map<String, Object> mapWrong = new HashMap<>();
        mapWrong.put("layoutBackground", R.drawable.note_wrong_backborder);
        mapWrong.put("content", "错词本");
        mapWrong.put("count", wrongCount);
        mapWrong.put("img", R.drawable.note_wrong);
        showResources.add(mapWrong);
        dbRaw.close();
        dbWrong.close();
    }

    private void initView(View view) {
        rvShow = view.findViewById(R.id.rcv_record_show);
        //rvWord = view.findViewById(R.id.rv_statistics_word);
        //rvPrecision = view.findViewById(R.id.rv_statistics_precision);
        tvWordFive = view.findViewById(R.id.tv_word_record_five);
        tvWordMonth = view.findViewById(R.id.tv_word_record_month);
        tvPrecisionFive = view.findViewById(R.id.tv_precision_record_five);
        tvPrecisionMonth = view.findViewById(R.id.tv_precision_record_month);
        //imageView = view.findViewById(R.id.iv_word);
        csb = view.findViewById(R.id.csb_record);

        llaccurrencyChart = view.findViewById(R.id.ll_accurencychart);
        llwordChart = view.findViewById(R.id.ll_wordchart);
        llacurrencyseach=view.findViewById(R.id.llaccrencychartsearch);
        llwordsearch=view.findViewById(R.id.llwordchartsearch);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Constant.point.y/3+50);
        llaccurrencyChart.setLayoutParams(params);
        llwordChart.setLayoutParams(params);
    }

    private void getAccurencyFiveRecord() {
        SharedPreferences sp = getActivity().getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        User user = new Gson().fromJson(sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("uid", user.getUid() + "").build();
        Request request = new Request.Builder().url(Constant.URL_GETRECORD_TOLINECHART_FIVE).post(fb).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                recordList = new Gson().fromJson(str, listType);
                Message message = new Message();
                message.what = Constant.ACCURENCY_FIVE;
                handler.sendMessage(message);
                Log.e("recordList", recordList + "");
            }
        });
    }

    private void getAccurencyMonthRecord() {
        SharedPreferences sp = getActivity().getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        User user = new Gson().fromJson(sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("uid", user.getUid() + "").build();
        Request request = new Request.Builder().url(Constant.URL_GETRECORD_TOLINECHART_MONTH).post(fb).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                recordList = new Gson().fromJson(str, listType);
                Message message = new Message();
                message.what = Constant.ACCURENCY_FIVE;
                handler.sendMessage(message);
                Log.e("recordList", recordList + "");
            }
        });
    }

    private void getWordFive(){
        List sumlist = new ArrayList();
        List datalist = new ArrayList();
        CorrectSumDBHelper correctSumDBHelper = new CorrectSumDBHelper(getContext(), "tbl_correctSumWord.db", 1);
        currectsumdatabase = correctSumDBHelper.getWritableDatabase();
        Cursor cursor2 = currectsumdatabase.query("TBL_CURRECTSUM", null, null, null, null, null, null);
        if (cursor2.moveToFirst()) {
            sumlist.add(cursor2.getInt(cursor2.getColumnIndex("SUM")) + "");
            Date date = null;
            try {
                date = new Date(simpleDateFormatt.parse(cursor2.getString(cursor2.getColumnIndex("ADDTIME"))).toString());
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM-dd");
                String sDate = simpleDateFormat1.format(date);
                datalist.add(sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }while (cursor2.moveToNext()) ;
        if (datalist.size() > 5) {
            sumlist.subList(0, 5);
            datalist.subList(0, 5);
        }
        Log.e("tt", sumlist.size() + ":" + datalist.size());
        String[] strSum = (String[]) sumlist.toArray(new String[sumlist.size()]);
        String[] strDate = (String[]) datalist.toArray(new String[datalist.size()]);
        String [] strY= new String[]{"0","20","40","60","80","100"};
        if(strSum.length!=0 && strDate.length!=0 && strY.length!=0){
            wordFiveView.SetInfo(strDate, // X轴刻度
                    strY, // Y轴刻度
                    strSum, // 数据
                    "", "个/天");
            llwordChart.setBackground(null);
            llwordChart.addView(wordFiveView);
            llwordsearch.setVisibility(View.VISIBLE);
          /*  tvWordFive.setClickable(true);
            tvWordMonth.setClickable(true);*/
        }else{
            llwordChart.setBackground(getResources().getDrawable(R.drawable.wordchartimg));
            llwordsearch.setVisibility(View.GONE);
         /*   tvWordFive.setClickable(false);
            tvWordMonth.setClickable(false);*/
        }
    }

    private void getWordMonth(){
        List sumlist = new ArrayList();
        List datalist = new ArrayList();
        CorrectSumDBHelper correctSumDBHelper = new CorrectSumDBHelper(getContext(), "tbl_correctSumWord.db", 1);
        currectsumdatabase = correctSumDBHelper.getWritableDatabase();
        String keyWord = "5" ; // 查询关键字 ，应该由方法定义
        String selectionArgs[] = new String[] { "%" + keyWord + "%", "%" + keyWord + "%" };
        String selection = "ADDTIME LIKE ?" ;
        Cursor cursor2 = currectsumdatabase.query("TBL_CURRECTSUM", null, selection, selectionArgs, null, null, null);
        if (cursor2.moveToFirst()) {
            sumlist.add(cursor2.getInt(cursor2.getColumnIndex("SUM")) + "");
            Date date = null;
            try {
                date = new Date(simpleDateFormatt.parse(cursor2.getString(cursor2.getColumnIndex("ADDTIME"))).toString());
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM");
                String sDate = simpleDateFormat1.format(date);
                datalist.add(sDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }while (cursor2.moveToNext()) ;
        if (datalist.size() > 5) {
            sumlist.subList(0, 5);
            datalist.subList(0, 5);
        }
        Log.e("tt", sumlist.size() + ":" + datalist.size());
        String[] strSum = (String[]) sumlist.toArray(new String[sumlist.size()]);
        String[] strDate = (String[]) datalist.toArray(new String[datalist.size()]);
        String [] strY= new String[]{"0","20","40","60","80","100"};
        if(strSum.length!=0 && strDate.length!=0 && strY.length!=0){
            wordFiveView.SetInfo(strDate, // X轴刻度
                    strY, // Y轴刻度
                    strSum, // 数据
                    "", "个/天");
            llwordChart.addView(wordFiveView);
        }
    }
}
