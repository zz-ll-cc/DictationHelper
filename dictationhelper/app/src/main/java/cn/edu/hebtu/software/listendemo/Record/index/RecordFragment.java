package cn.edu.hebtu.software.listendemo.Record.index;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.CustomScrollBar;
import cn.edu.hebtu.software.listendemo.Untils.NewWordDBHelper;
import cn.edu.hebtu.software.listendemo.Untils.WrongWordDBHelper;

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);
        SharedPreferences sp = getActivity().getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        User user = new Gson().fromJson(sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        initView(view);
        initData();
        initAdapter();
        return view;
    }

    private void initAdapter() {
//        adapter = new StatisticAdapter(R.layout.fragment_statistics_detail, urlList, getContext());
        showAdapter = new RecordShowAdapter(getContext(),R.layout.fragment_record_recycler_item,showResources);
        rvShow.setAdapter(showAdapter);
        csb.setText("已连续学习5天");
    }

    private void initRecycler() {
//        SmoothScrollLayoutManager layoutManager = new SmoothScrollLayoutManager(getContext());
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        rvWord.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        showResources.clear();
        initData();
        showAdapter.notifyDataSetChanged();
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
        rvWord = view.findViewById(R.id.rv_statistics_word);
        rvPrecision = view.findViewById(R.id.rv_statistics_precision);
        tvWordFive = view.findViewById(R.id.tv_word_record_five);
        tvWordMonth = view.findViewById(R.id.tv_word_record_month);
        tvPrecisionFive = view.findViewById(R.id.tv_precision_record_five);
        tvPrecisionMonth = view.findViewById(R.id.tv_precision_record_month);
        imageView = view.findViewById(R.id.iv_word);
        csb = view.findViewById(R.id.csb_record);
    }


}
