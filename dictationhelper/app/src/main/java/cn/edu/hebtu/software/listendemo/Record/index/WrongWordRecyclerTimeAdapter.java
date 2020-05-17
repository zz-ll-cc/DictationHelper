package cn.edu.hebtu.software.listendemo.Record.index;

import android.content.Context;
import android.graphics.Color;
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

import cn.edu.hebtu.software.listendemo.Entity.NewOrWrongTimeWord;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;

public class WrongWordRecyclerTimeAdapter extends RecyclerView.Adapter {
    private List<NewOrWrongTimeWord> newWordsList;
    private Context context;
    private int layout_item_id;
    private ImageView ivEmpty;
    private RelativeLayout rlEmpty;
    private LinearLayout llHave;

    public WrongWordRecyclerTimeAdapter(List<NewOrWrongTimeWord> newWordsList, Context context, int layout_item_id, ImageView ivEmpty, RelativeLayout rlEmpty, LinearLayout llHave) {
        this.newWordsList = newWordsList;
        this.context = context;
        this.layout_item_id = layout_item_id;
        this.ivEmpty = ivEmpty;
        this.rlEmpty = rlEmpty;
        this.llHave = llHave;
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
        NewOrWrongTimeWord timeWord = newWordsList.get(i);
        if (newWordsList.isEmpty()){
            rlEmpty.setVisibility(View.VISIBLE);
            ivEmpty.setImageResource(R.drawable.empty_wrong);
            rlEmpty.setBackgroundColor(Color.parseColor("#FFFFFF"));
            llHave.setVisibility(View.GONE);
        }else{
            rlEmpty.setVisibility(View.GONE);
            llHave.setVisibility(View.VISIBLE);
            List<Word> words = timeWord.getWords();
            holder.tvCount.setText(timeWord.getWords().size() + "");
            holder.tvTime.setText(timeWord.getTime());
            WrongWordRecyclerViewAdapter adapter = new WrongWordRecyclerViewAdapter(context, words, R.layout.activity_record_neworwrongword_item, ivEmpty, rlEmpty, llHave, timeWord.getTime(),holder.tvCount);
            holder.rvWords.setAdapter(adapter);
        }
    }

    public void deleteItem(String time) {
        for (int i = 0; i < newWordsList.size(); i++) {
            if (newWordsList.get(i).getTime().equals(time)) {
                newWordsList.remove(i);
                break;
            }
        }
        if (newWordsList.isEmpty()){
            rlEmpty.setVisibility(View.VISIBLE);
            ivEmpty.setImageResource(R.drawable.empty_wrong);
            rlEmpty.setBackgroundColor(Color.parseColor("#FFFFFF"));
            llHave.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (newWordsList != null) {
            return newWordsList.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvCount;
        RecyclerView rvWords;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_record_item_time);
            tvCount = itemView.findViewById(R.id.tv_record_item_count);
            rvWords = itemView.findViewById(R.id.rcv_record_item_out);
        }
    }
}
