package cn.edu.hebtu.software.listendemo.Mine.index.cardbag;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Inventory;
import cn.edu.hebtu.software.listendemo.R;

public class CreditBagDetailRecyclerAdapter extends RecyclerView.Adapter {
    private List<Inventory> inventories;
    private Context context;
    private int layout_item_id;

    public CreditBagDetailRecyclerAdapter(List<Inventory> inventories, Context context, int layout_item_id) {
        this.inventories=inventories;
        this.context = context;
        this.layout_item_id = layout_item_id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(layout_item_id,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        Inventory inventorie=inventories.get(i);
        holder.tvCardTagName.setText("");
        holder.tvCardBagItemDescripe.setText("");
        holder.tvCardBagExpirationTime.setText(inventorie.getExpiryTime());
        holder.tvCreditBagUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != inventories){
            return inventories.size();
        }else {
            return 0;
        }
    }
    public void changeDataSource(List<Inventory> inventories) {
        this.inventories=inventories;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
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
            tvCreditBagUse=itemView.findViewById(R.id.tv_credit_bag_usetv_credit_bag_use);
            tvCardBagExpirationTime=itemView.findViewById(R.id.tv_card_bag_expiration_time);

        }
    }
}
