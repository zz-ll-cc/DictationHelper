package cn.edu.hebtu.software.listendemo.Mine.index.credit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.CreditRecord;
import cn.edu.hebtu.software.listendemo.Entity.MonthCreditRecord;
import cn.edu.hebtu.software.listendemo.R;

public class CreditDetailRecyclerAdapter extends RecyclerView.Adapter {
    private List<CreditRecord> creditRecords;
    private Context context;
    private int layout_item_id;

    public CreditDetailRecyclerAdapter(List<CreditRecord> creditRecords, Context context, int layout_item_id) {
        this.creditRecords = creditRecords;
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
        CreditRecord creditRecord = creditRecords.get(i);
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(creditRecord.getCreateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String time = new SimpleDateFormat("yyyy年MM月dd日").format(date);
        String count = creditRecord.getIncrement()+"";
        if (creditRecord.getIncrement()>0){
            count = "+"+creditRecord.getIncrement();
        }
        holder.tvCount.setText(count);
        holder.tvReason.setText(creditRecord.getReason());
        holder.tvTime.setText(time);
    }

    @Override
    public int getItemCount() {
        if (null != creditRecords){
            return creditRecords.size();
        }else {
            return 0;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvReason;
        TextView tvTime;
        TextView tvCount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCount = itemView.findViewById(R.id.tv_credit_detail_item_count);
            tvTime = itemView.findViewById(R.id.tv_credit_detail_item_cretime);
            tvReason = itemView.findViewById(R.id.tv_credit_detail_item_reason);
        }
    }
}
