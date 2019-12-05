package cn.edu.hebtu.software.listendemo.Mine.index;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Mine.index.settings.EditMsgActivity;
import cn.edu.hebtu.software.listendemo.Mine.index.settings.EidtCenterActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

import static android.content.Context.MODE_PRIVATE;


public class MyInfoFragment extends Fragment {
    private View view;
    private RelativeLayout rlEditMyMsg;
    private RelativeLayout rlNoLogin;
    private ImageView ivHeader;
    private TextView tvName;
    private ImageView ivVip;
    private LinearLayout llBalance;
    private TextView tvBalance;
    private LinearLayout llIntegral;
    private TextView tvIntegral;
    private LinearLayout llMedal;
    private TextView tvMedal;
    private RelativeLayout rlEnterVip;
    private TextView tvVip;
    private RelativeLayout rlEnterMedal;
    private RelativeLayout rlEnterIntegral;
    private RelativeLayout rlEnterBuyRec;
    private RelativeLayout rlEnterEdit;
    private SharedPreferences sp;
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_info, container, false);
        findViews();
        initData();
        setData();
        setListener();
        SharedPreferences sp = getActivity().getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        user = gson.fromJson(sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
        Log.e("MyInfoFragment",""+user.toString());

        return view;
    }

    private void setListener() {
        MyInfoListener listener = new MyInfoListener();
        rlEditMyMsg.setOnClickListener(listener);
        rlEnterEdit.setOnClickListener(listener);
    }

    private class MyInfoListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_fragment_my_edit_header:
                    Intent intent1 = new Intent(getContext(), EditMsgActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.rl_fragment_my_enter_setting:
                    Intent intent = new Intent(getContext(), EidtCenterActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    private void setData() {
        if (user.getUid() == 0) {
            rlNoLogin.setVisibility(View.VISIBLE);
            rlEditMyMsg.setVisibility(View.GONE);
        } else {
            rlNoLogin.setVisibility(View.GONE);
            rlEditMyMsg.setVisibility(View.VISIBLE);
            tvName.setText(user.getUname());
            if (user.getVip() == User.isVip) {
                tvVip.setText(Constant.TV_IS_VIP_SHOW);
                ivVip.setImageDrawable(getResources().getDrawable(R.drawable.vip_is));
                tvVip.setTextColor(Color.parseColor("#FFC400"));
            } else {
                tvVip.setText(Constant.TV_NOT_VIP_SHOW);
                ivVip.setImageDrawable(getResources().getDrawable(R.drawable.vip_no));
                tvVip.setTextColor(Color.parseColor("#ff0000"));
            }
            RequestOptions ro = new RequestOptions().error(R.drawable.head_user).circleCrop();
            Glide.with(this).load(user.getUheadPath()).apply(ro).into(ivHeader);
        }
    }

    private void initData() {
        sp = getContext().getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        user = gson.fromJson(sp.getString(Constant.USER_KEEP_KEY, Constant.DEFAULT_KEEP_USER), User.class);
    }

    private void findViews() {
        rlEditMyMsg = view.findViewById(R.id.rl_fragment_my_edit_header);
        rlNoLogin = view.findViewById(R.id.rl_fragment_my_edit_header_unlogin);
        ivHeader = view.findViewById(R.id.iv_fragment_my_header);
        tvName = view.findViewById(R.id.tv_fragment_my_nickname);
        ivVip = view.findViewById(R.id.iv_fragment_my_vip);
        llBalance = view.findViewById(R.id.ll_fragment_my_balance);
        llIntegral = view.findViewById(R.id.ll_fragment_my_integral);
        llMedal = view.findViewById(R.id.ll_fragment_my_medal);
        tvBalance = view.findViewById(R.id.tv_fragment_my_balance);
        tvIntegral = view.findViewById(R.id.tv_fragment_my_integral);
        tvMedal = view.findViewById(R.id.tv_fragment_my_medal);
        rlEnterVip = view.findViewById(R.id.rl_fragment_my_enter_vip);
        tvVip = view.findViewById(R.id.tv_fragment_my_vip);
        rlEnterMedal = view.findViewById(R.id.rl_fragment_my_enter_medalwall);
        rlEnterIntegral = view.findViewById(R.id.rl_fragment_my_enter_integral);
        rlEnterBuyRec = view.findViewById(R.id.rl_fragment_my_enter_buy_record);
        rlEnterEdit = view.findViewById(R.id.rl_fragment_my_enter_setting);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        setData();
    }
}
