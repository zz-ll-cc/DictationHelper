package cn.edu.hebtu.software.listendemo.Host.listenWord;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;

public class ListenResultSelectRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Word> paperlist;
    private int itemId;
    private Map<Integer, Boolean> checkStatus;//用来记录所有checkbox的状态

    public ListenResultSelectRecyclerViewAdapter(Context context, List<Word> paperlist, int itemId,Map<Integer, Boolean> checkStatus) {
        this.context = context;
        this.paperlist = paperlist;
        this.itemId = itemId;
        this.checkStatus=checkStatus;
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
        itemViewHolder.tvChinese.setText(paperlist.get(i).getWchinese());
        itemViewHolder.tvEnglish.setText(paperlist.get(i).getWenglish());
        itemViewHolder.ivFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == checkStatus.get(i)){    // 此时没有添加
                    itemViewHolder.rlOut.setBackgroundColor(Color.parseColor("#9AFFE6B6"));
                    Glide.with(context).load(R.drawable.page_result_false_choose).into(itemViewHolder.ivFalse);
                    Glide.with(context).load(R.drawable.page_result_true).into(itemViewHolder.ivTrue);
                    checkStatus.put(i,false);
                }else if (null != checkStatus.get(i) && checkStatus.get(i) == true){
                    itemViewHolder.rlOut.setBackgroundColor(Color.parseColor("#9AFFE6B6"));
                    Glide.with(context).load(R.drawable.page_result_false_choose).into(itemViewHolder.ivFalse);
                    Glide.with(context).load(R.drawable.page_result_true).into(itemViewHolder.ivTrue);
                    checkStatus.put(i,false);
                }
            }
        });
        itemViewHolder.ivTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == checkStatus.get(i)){    // 此时没有添加
                    itemViewHolder.rlOut.setBackgroundColor(Color.parseColor("#9AFFF5F1"));
                    Glide.with(context).load(R.drawable.page_result_false).into(itemViewHolder.ivFalse);
                    Glide.with(context).load(R.drawable.page_result_true_choose).into(itemViewHolder.ivTrue);
                    checkStatus.put(i,true);
                }else if (null != checkStatus.get(i) && checkStatus.get(i) == false){
                    itemViewHolder.rlOut.setBackgroundColor(Color.parseColor("#9AFFF5F1"));
                    Glide.with(context).load(R.drawable.page_result_false).into(itemViewHolder.ivFalse);
                    Glide.with(context).load(R.drawable.page_result_true_choose).into(itemViewHolder.ivTrue);
                    checkStatus.put(i,true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (paperlist != null) {
            return paperlist.size();
        }
        return 0;
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvChinese;
        public TextView tvEnglish;
        public ImageView ivTrue;
        public ImageView ivFalse;
        public RelativeLayout rlOut;

        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChinese = itemView.findViewById(R.id.tv_paper_listen_item_chinese);
            tvEnglish = itemView.findViewById(R.id.tv_paper_listen_item_english);
            ivTrue = itemView.findViewById(R.id.iv_page_listen_item_true);
            ivFalse = itemView.findViewById(R.id.iv_page_listen_item_false);
            rlOut = itemView.findViewById(R.id.rl_paper_listen_item_out);
        }
    }


}
