package cn.edu.hebtu.software.listendemo.Record.index;

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

import cn.edu.hebtu.software.listendemo.R;

public class RecordShowAdapter extends RecyclerView.Adapter {
    private Context context;
    private int layout_item_id;
    private List<Map<String, Object>> res;

    public RecordShowAdapter(Context context, int layout_item_id, List<Map<String, Object>> res) {
        this.context = context;
        this.layout_item_id = layout_item_id;
        this.res = res;
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
        holder.tvCount.setText((int) map.get("count") + "");
        holder.tvName.setText(map.get("content") + "");
        holder.ivImg.setImageDrawable(context.getResources().getDrawable((Integer) map.get("img")));
        holder.llBackGround.setBackgroundResource((Integer) map.get("layoutBackground"));
        holder.rlOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((map.get("content") + "").equals("生词本")) {
                    Intent intent = new Intent(context,NewWordActivity.class);
                    context.startActivity(intent);
                } else if ((map.get("content") + "").equals("错词本")) {
                    Intent intent = new Intent(context,WrongWordActivity.class);
                    context.startActivity(intent);
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
        public TextView tvCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rlOut = itemView.findViewById(R.id.rl_rec_item_out);
            llBackGround = itemView.findViewById(R.id.ll_rec_item_img_back);
            ivImg = itemView.findViewById(R.id.iv_rec_item_img);
            tvName = itemView.findViewById(R.id.tv_rec_item_name);
            tvCount = itemView.findViewById(R.id.tv_rec_item_count);
        }
    }
}
