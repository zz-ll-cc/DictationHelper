package cn.edu.hebtu.software.listendemo.Mine.index.cardbag;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.edu.hebtu.software.listendemo.Entity.Inventory;
import cn.edu.hebtu.software.listendemo.R;

public class CreditBagDetailRecyclerAdapter extends RecyclerView.Adapter {
    private List<Inventory> inventories;
    private Context context;
    private int layout_item_id;

    public CreditBagDetailRecyclerAdapter(List<Inventory> inventories, Context context, int layout_item_id) {
        this.inventories = inventories;
        this.context = context;
        this.layout_item_id = layout_item_id;
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
        Inventory inventory = inventories.get(i);
        Log.e("ttttttttttk", inventory.toString());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(Calendar.getInstance().getTime());
        try {
            Date nowDate = format.parse(time);
            String expendTime = inventory.getExpiryTime();
            String[] arr = expendTime.split("T");
            String[] tarr = arr[1].split(".000");
            String dd = arr[0] + " " + tarr[0];
            Date expiryDate = format.parse(dd);
//            Log.e("tttttttttt1", nowDate.toString() + "   " + expiryDate.toString());
//            Log.e("tttttttttt2", nowDate.getTime() + "   " + expiryDate.getTime());
            if (inventory.getIsUsed() == 0) {
                if (nowDate.getTime() > expiryDate.getTime()) {
                    Log.e("tttttttttt", "已过期");
                    holder.llBackground.setBackground(context.getResources().getDrawable(R.drawable.coupon_gray));
                    holder.tvCreditBagUse.setText("已过期");
                }
                if (nowDate.getTime() < expiryDate.getTime()) {
//                    Log.e("tttttttttt", i + "  " + "去使用");
                    holder.llBackground.setBackground(context.getResources().getDrawable(R.drawable.coupon1));
                    holder.tvCreditBagUse.setText("去使用");
                }
            }
            if (inventory.getIsUsed() == 1) {
                holder.llBackground.setBackground(context.getResources().getDrawable(R.drawable.coupon1));
                holder.tvCreditBagUse.setText("已使用");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tvCardTagName.setText(inventory.getName());
        holder.tvCardBagItemDescripe.setText(inventory.getItem().getDescription());
        String date = inventory.getExpiryTime();
        String[] arr = date.split("T");
        String[] tarr = arr[1].split(".000");
        String dd = arr[0] + " " + tarr[0];
        holder.tvCardBagExpirationTime.setText(dd);
        holder.tvCreditBagUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != inventories) {
            return inventories.size();
        } else {
            return 0;
        }
    }

    public void changeDataSource(List<Inventory> inventories) {
        this.inventories = inventories;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llBackground;
        TextView tvCardTagName;
        TextView tvCardBagItemDescripe;
        TextView tvCreditBagUse;
        TextView tvCardBagExpirationTime;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            llBackground = itemView.findViewById(R.id.ll_background);
            tvCardTagName = itemView.findViewById(R.id.tv_card_bag_item_name);
            tvCardBagItemDescripe = itemView.findViewById(R.id.tv_card_bag_item_descripe);
            tvCreditBagUse = itemView.findViewById(R.id.tv_credit_bag_usetv_credit_bag_use);
            tvCardBagExpirationTime = itemView.findViewById(R.id.tv_card_bag_expiration_time);

        }
    }

    /**
     * 获取十六进制的颜色代码.例如  "#6E36B4" , For HTML ,
     *
     * @return String
     */
    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        return r + g + b;
    }
}
