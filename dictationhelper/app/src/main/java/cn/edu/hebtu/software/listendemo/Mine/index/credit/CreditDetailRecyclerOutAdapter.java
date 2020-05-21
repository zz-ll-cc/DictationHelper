package cn.edu.hebtu.software.listendemo.Mine.index.credit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.MonthCreditRecord;
import cn.edu.hebtu.software.listendemo.R;

public class CreditDetailRecyclerOutAdapter extends RecyclerView.Adapter {
    private List<MonthCreditRecord> monthCreditRecords;
    private Context context;
    private int layout_item_id;

    public CreditDetailRecyclerOutAdapter(List<MonthCreditRecord> monthCreditRecords, Context context, int layout_item_id) {
        this.monthCreditRecords = monthCreditRecords;
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
        MonthCreditRecord monthCreditRecord = monthCreditRecords.get(i);
        CreditDetailRecyclerAdapter adapter = new CreditDetailRecyclerAdapter(monthCreditRecord.getCreditRecords(),context,R.layout.activity_credit_detail_item);
        holder.rcvOut.setAdapter(adapter);

        holder.tvTime.setText(monthCreditRecord.getRecordTime());
    }

    @Override
    public int getItemCount() {
        if (null != monthCreditRecords) {
            return monthCreditRecords.size();
        } else {
            return 0;
        }
    }

    public void changeDataSource(List<MonthCreditRecord> monthCreditRecords) {
        this.monthCreditRecords = monthCreditRecords;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        RecyclerView rcvOut;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_credit_detail_item_time);
            rcvOut = itemView.findViewById(R.id.rcv_credit_detail_item_out);
            rcvOut.addItemDecoration(new SpacesItemDecoration(5));
        }
    }
}
