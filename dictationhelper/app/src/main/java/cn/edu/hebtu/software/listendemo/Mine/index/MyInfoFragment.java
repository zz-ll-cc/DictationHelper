package cn.edu.hebtu.software.listendemo.Mine.index;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Mine.index.settings.EditMsgActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.StatusBarUtil;

import static android.content.Context.MODE_PRIVATE;


public class MyInfoFragment extends Fragment {
    private View view;
    private RelativeLayout rlEditMyMsg;
    private ImageView ivHeader;
    private TextView tvName;
    private SharedPreferences sp;
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private User user;
    private RecyclerView rvShow;
    private List<Map<String, Object>> showRes = new ArrayList<>();
    private MyShowAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_info, container, false);
        findViews();
        initData();
        setData();
        setListener();

        return view;
    }

    private void setListener() {
        MyInfoListener listener = new MyInfoListener();
        rlEditMyMsg.setOnClickListener(listener);
    }

    private class MyInfoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_fragment_my_edit_header:
                    Intent intent1 = new Intent(getContext(), EditMsgActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    }

    private void setData() {
        rlEditMyMsg.setVisibility(View.VISIBLE);
        tvName.setText(user.getUname());
        RequestOptions ro = new RequestOptions().error(R.drawable.head_user).circleCrop();
        Glide.with(this).load(user.getUheadPath()).apply(ro).into(ivHeader);
        adapter = new MyShowAdapter(R.layout.fragment_my_recycler_item, showRes, getContext());
        rvShow.setAdapter(adapter);
    }

    private void initData() {
        sp = getContext().getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        user = gson.fromJson(sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        showRes.clear();
        Map<String, Object> mapIntegral = new HashMap<>();
        mapIntegral.put("imgBg", R.drawable.my_integral_border);
        mapIntegral.put("img", R.drawable.integral);
        mapIntegral.put("name", "我的积分");
        mapIntegral.put("content", "0");

        Map<String, Object> mapMessage = new HashMap<>();
        mapMessage.put("imgBg", R.drawable.my_message_border);
        mapMessage.put("img", R.drawable.message);
        mapMessage.put("name", "消息通知");
        mapMessage.put("content", "0");

        Map<String, Object> mapShop = new HashMap<>();
        mapShop.put("imgBg", R.drawable.my_shopping_border);
        mapShop.put("img", R.drawable.shop);
        mapShop.put("name", "积分商城");
        mapShop.put("content", "正在开发");

        Map<String, Object> mapSetting = new HashMap<>();
        mapSetting.put("imgBg", R.drawable.my_setting_border);
        mapSetting.put("img", R.drawable.setting);
        mapSetting.put("name", "设置中心");
        mapSetting.put("content", "");
        mapSetting.put("nameMargin",35);
        showRes.add(mapIntegral);
        showRes.add(mapMessage);
        showRes.add(mapShop);
        showRes.add(mapSetting);
    }

    private void findViews() {
        rlEditMyMsg = view.findViewById(R.id.rl_fragment_my_edit_header);
        ivHeader = view.findViewById(R.id.iv_fragment_my_header);
        tvName = view.findViewById(R.id.tv_fragment_my_nickname);
        rvShow = view.findViewById(R.id.rcv_my_show);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        setData();
        StatusBarUtil.setStatusBarColor(getActivity(),R.color.yellow);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColor(getActivity(),R.color.yellow);
    }

    @Override
    public void onStop() {
        super.onStop();
        StatusBarUtil.setStatusBarColor(getActivity(),R.color.white);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StatusBarUtil.setStatusBarColor(getActivity(),R.color.white);
    }
}
