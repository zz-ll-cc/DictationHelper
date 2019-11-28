package cn.edu.hebtu.software.listendemo.Host.learnWord;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;

public class LearnWordRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Word> learnWords;
    private int itemId;

    public LearnWordRecyclerViewAdapter(Context context, List  learnWords, int itemId) {
        this.context = context;
        this. learnWords= learnWords;
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(itemId,viewGroup,false);
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        //设置每一项所显示的内容
        final MyItemViewHolder itemViewHolder=(MyItemViewHolder) viewHolder;

        itemViewHolder.tvWordChinese.setText( learnWords.get(i).getWchinese().toString());
        itemViewHolder.tvWordEnglish.setText(learnWords.get(i).getWenglish().toString());
        itemViewHolder.tvSum.setText( learnWords.size()+"");
        itemViewHolder.tvCurrent.setText(i+1+"");
        Glide.with(context).load(learnWords.get(i).getWimgPath()).into(itemViewHolder.ivWordImg);
    }

    @Override
    public int getItemCount() {
        if(learnWords!=null){
            return learnWords.size();
        }
        return 0;
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public TextView tvWordChinese;
        public TextView tvWordEnglish;
        public ImageView ivWordImg;
        public TextView tvCurrent;
        public TextView tvSum;
        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWordChinese=itemView.findViewById(R.id.tv_wordChinese);
            tvWordEnglish=itemView.findViewById(R.id.tv_wordEnglish);
            ivWordImg=itemView.findViewById(R.id.iv_wordImg);
            root=itemView.findViewById(R.id.ll_above);
            tvCurrent=itemView.findViewById(R.id.tv_current);
            tvSum=itemView.findViewById(R.id.tv_sum);
        }
    }



}
