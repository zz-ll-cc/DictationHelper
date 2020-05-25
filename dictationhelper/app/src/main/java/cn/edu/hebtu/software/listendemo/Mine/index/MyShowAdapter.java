package cn.edu.hebtu.software.listendemo.Mine.index;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Mine.index.credit.SyllabusActivity;
import cn.edu.hebtu.software.listendemo.Mine.index.settings.EidtCenterActivity;
import cn.edu.hebtu.software.listendemo.Mine.index.shopping.ShoppingActivity;
import cn.edu.hebtu.software.listendemo.R;

public class MyShowAdapter extends RecyclerView.Adapter {
    private int layout_item_id;
    private List<Map<String, Object>> res;
    private Context context;

    public MyShowAdapter(int layout_item_id, List<Map<String, Object>> res, Context context) {
        this.layout_item_id = layout_item_id;
        this.res = res;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(layout_item_id, viewGroup, false);
        // 获取屏幕宽度
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int width = (widthPixels - 20) / 2;
        int height = width * 6 / 5;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.setMargins(10, 5, 10, 5);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        Map<String, Object> map = res.get(i);

        holder.llBackGround.setBackgroundResource((Integer) map.get("imgBg"));
        holder.tvName.setText(map.get("name").toString());
        holder.ivImg.setImageResource((Integer) map.get("img"));
        holder.tvContent.setText(map.get("content").toString());
        if (map.containsKey("nameMargin")){
            holder.tvName.setPadding(0, (Integer) map.get("nameMargin"),0,0);
        }
        holder.rlOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (map.get("name").toString()){
                    case "我的积分":
                        Intent intent1 = new Intent(context, SyllabusActivity.class);
                        context.startActivity(intent1);
                        break;
                    case "消息通知":
                        Intent intent2 = new Intent();
                        break;
                    case "积分商城":
                        Intent intent3 = new Intent(context, ShoppingActivity.class);
                        context.startActivity(intent3);
                        break;
                    case "设置中心":
                        Intent intent4 = new Intent(context, EidtCenterActivity.class);
                        context.startActivity(intent4);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == res)
            return 0;
        else return res.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlOut;
        public LinearLayout llBackGround;
        public ImageView ivImg;
        public TextView tvName;
        public TextView tvContent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rlOut = itemView.findViewById(R.id.rl_rec_item_out);
            llBackGround = itemView.findViewById(R.id.ll_rec_item_img_back);
            ivImg = itemView.findViewById(R.id.iv_rec_item_img);
            tvName = itemView.findViewById(R.id.tv_rec_item_name);
            tvContent = itemView.findViewById(R.id.tv_my_rec_content);
        }
    }
}
