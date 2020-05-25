package cn.edu.hebtu.software.listendemo.Mine.index.shopping;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Item;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShopRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Item> itemList;
    private int layout_item_id;
    private User user;

    public ShopRecyclerAdapter(Context context, List<Item> itemList, int layout_item_id, User user) {
        this.context = context;
        this.itemList = itemList;
        this.layout_item_id = layout_item_id;
        this.user = user;
    }

    public void updateUser(User user) {
        this.user = user;
        notifyDataSetChanged();
    }

    public void changeDataSource(List<Item> items) {
        this.itemList = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(layout_item_id, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        Item item = itemList.get(i);
        Glide.with(context).load(item.getCover()).into(holder.ivCover);
        holder.tvName.setText(item.getName());
        holder.pbLeft.setMax(item.getQuantity());
        holder.pbLeft.setProgress(item.getLeft());
        holder.tvLeftCount.setText(item.getLeft()+"");
        holder.rlBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowBuyDialog dialog = ShowBuyDialog.getDialog();
                dialog.setContext(context);
                dialog.setItem(item);
                dialog.setUser(user);
                dialog.showDialog();
            }
        });
        if (item.getLeft() != 0){
            holder.rlBuy.setEnabled(true);
            holder.rlBuy.setBackgroundResource(R.drawable.btn_bg_shop_buy);
            holder.tvCost.setTextColor(Color.parseColor("#ffffff"));
            holder.tvCost.setText(item.getPrice()+" 积分兑换");
        }else{
            holder.rlBuy.setEnabled(false);
            holder.rlBuy.setBackgroundResource(R.drawable.btn_bg_shop_buy_no);
            holder.tvCost.setTextColor(Color.parseColor("#e7e7e7"));
            holder.tvCost.setText("已售罄");
        }

    }

    @Override
    public int getItemCount() {
        if (null == itemList)
            return 0;
        else
            return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlOut;
        ImageView ivCover;
        ProgressBar pbLeft;
        TextView tvLeftCount;
        TextView tvName;
        RelativeLayout rlBuy;
        TextView tvCost;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rlOut = itemView.findViewById(R.id.rl_shopping_item_out);
            ivCover = itemView.findViewById(R.id.iv_shopping_item_cover);
            pbLeft = itemView.findViewById(R.id.progress_shop_item_surplus);
            tvLeftCount = itemView.findViewById(R.id.tv_shop_item_surplus_count);
            tvName = itemView.findViewById(R.id.tv_shop_item_name);
            rlBuy = itemView.findViewById(R.id.rl_shopping_item_buy);
            tvCost = itemView.findViewById(R.id.tv_shop_item_cost);
        }
    }
}
