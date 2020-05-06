package cn.edu.hebtu.software.listendemo.Host.searchWord;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.HistroyWord;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;

import static cn.edu.hebtu.software.listendemo.Untils.SearchHistoryDBHelper.TBL_HISTORY;

public class ResultRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Word> wordList ;
    private int layout_item_id;
    private SQLiteDatabase historyDB;

    public ResultRecyclerAdapter(Context context, List<Word> wordList, int layout_item_id, SQLiteDatabase db) {
        this.context = context;
        this.wordList = wordList;
        this.layout_item_id = layout_item_id;
        this.historyDB = db;
    }

    public void updateWordList(List<Word> wordList){
        this.wordList = wordList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(layout_item_id, viewGroup, false);
        return new ResultRecyclerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        final Word word = wordList.get(i);
        Log.e("resultAdapterWord",word.toString());
        myViewHolder.tvEnglish.setText(word.getWenglish());
        myViewHolder.tvChinese.setText(word.getWchinese());
        myViewHolder.rlOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. 删除数据库中源数据
               historyDB.delete(TBL_HISTORY,"wid = ?",new String[]{word.getWid()+""});
                // 2. 将词条数据插入数据库
                ContentValues cv = new ContentValues();
                cv.put("wid",word.getWid());
                cv.put("english",word.getWenglish());
                cv.put("chinese",word.getWchinese());
                historyDB.insert(TBL_HISTORY,null,cv);
                // 3. 跳转至展示单个界面
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
        RelativeLayout rlOut;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChinese = itemView.findViewById(R.id.tv_search_result_chinese);
            tvEnglish = itemView.findViewById(R.id.tv_search_result_english);
            rlOut = itemView.findViewById(R.id.rl_result_item_out);
        }
    }
}
