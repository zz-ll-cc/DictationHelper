package cn.edu.hebtu.software.listendemo.Host.listenResult;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

public class ListenResultRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Word> listenWordOrg;
    private List<Word> listenWordYour;
    private int itemId;
    public ListenResultRecyclerViewAdapter(Context context, List<Word> listenWordOrg, List<Word> listenWordYour, int itemId) {
        this.context = context;
        this.listenWordOrg = listenWordOrg;
        this.listenWordYour = listenWordYour;
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(itemId, viewGroup, false);
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        //设置每一项所显示的内容
        MyItemViewHolder itemViewHolder = (MyItemViewHolder) viewHolder;
        itemViewHolder.tvYour.setVisibility(View.GONE);
        itemViewHolder.tvChinese.setText(listenWordOrg.get(i).getWchinese());
        itemViewHolder.tvEnglish.setText(listenWordOrg.get(i).getWenglish());
        if (listenWordYour.get(i).getIsTrue() != 0 ){
            itemViewHolder.ivRight.setImageResource(R.drawable.result_right);
        }else{
            itemViewHolder.ivRight.setImageResource(R.drawable.result_wrong);
        }

        if (listenWordYour.get(i).getIsTrue() == Constant.SPELL_FALSE) {
            itemViewHolder.tvYour.setText(listenWordYour.get(i).getWenglish());
        }
        if (listenWordYour.get(i).getWenglish().equals("")) {
            itemViewHolder.tvYour.setText(Constant.NO_STYLE);
        } else {
            itemViewHolder.tvYour.setText(listenWordYour.get(i).getWenglish());
        }
        itemViewHolder.rlOut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (listenWordYour.get(i).getIsTrue() == 0){
                    if (itemViewHolder.tvYour.getVisibility() == View.VISIBLE){
                        itemViewHolder.tvYour.setVisibility(View.GONE);
                    }else if(itemViewHolder.tvYour.getVisibility() == View.GONE){
                        itemViewHolder.tvYour.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listenWordOrg != null) {
            return listenWordOrg.size();
        }
        return 0;
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlOut;
        public TextView tvChinese ;
        public TextView tvEnglish ;
        public TextView tvYour;
        public ImageView ivRight;

        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            rlOut = itemView.findViewById(R.id.rl_result_item_show);
            tvChinese = itemView.findViewById(R.id.tv_result_item_chinese);
            tvEnglish = itemView.findViewById(R.id.tv_result_item_english);
            tvYour = itemView.findViewById(R.id.tv_result_item_your);
            ivRight = itemView.findViewById(R.id.iv_result_item_right);
        }
    }


}
