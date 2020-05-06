package cn.edu.hebtu.software.listendemo.Host.searchWord;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.HistroyWord;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.bookDetail.UnitRecyclerAdapter;
import cn.edu.hebtu.software.listendemo.R;

import static cn.edu.hebtu.software.listendemo.Untils.SearchHistoryDBHelper.TBL_HISTORY;

public class HistroyRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HistroyWord> wordList ;
    private int layout_item_id;
    private TextView tvClean;
    private SQLiteDatabase histroyDB;
    public HistroyRecyclerAdapter(Context context, List<HistroyWord> wordList, int layout_item_id, TextView tvClean, SQLiteDatabase db) {
        this.context = context;
        this.wordList = wordList;
        this.layout_item_id = layout_item_id;
        this.tvClean = tvClean;
        this.histroyDB = db;
    }

    public void updateWordList(List<HistroyWord> wordList){
        this.wordList = wordList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(layout_item_id, viewGroup, false);
        return new HistroyRecyclerAdapter.MyViewHolder(view);
    }

    public void setCleanAllListener(){
        tvClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 删除数据
                wordList.clear();
                // 2. 删库
                histroyDB.delete(TBL_HISTORY,null,null);
                // 3. 通知
                tvClean.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (wordList.size() == 0){
            tvClean.setVisibility(View.GONE);
        }else {
            tvClean.setVisibility(View.VISIBLE);
        }
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        final HistroyWord word = wordList.get(i);
        myViewHolder.tvChinese.setText(word.getChinese());
        myViewHolder.tvEnglish.setText(word.getEnglish());
        myViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 当点击时，删除此项
                wordList.remove(word);
                // 数据库中删除此项历史记录
                histroyDB.delete(TBL_HISTORY,"id = ?",new String[]{word.getId()+""});
                // 通知改变
                if (wordList.size() == 0){
                    tvClean.setVisibility(View.GONE);
                }else{
                    tvClean.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        });
        myViewHolder.rlOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击进入单个单词展示界面
                EventBus.getDefault().post(word);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == wordList || wordList.isEmpty()){
            return 0;
        }else{
            return wordList.size();
        }
    }

    class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView tvChinese;
        TextView tvEnglish;
        ImageView ivDelete;
        RelativeLayout rlOut;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChinese = itemView.findViewById(R.id.tv_search_history_chinese);
            tvEnglish = itemView.findViewById(R.id.tv_search_history_english);
            ivDelete = itemView.findViewById(R.id.iv_search_history_delete);
            rlOut = itemView.findViewById(R.id.rl_history_item_out);
        }
    }
}
