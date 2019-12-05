package cn.edu.hebtu.software.listendemo.Record.index;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.NewWordDBHelper;


public class NewWordRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Word> words;
    private int itemId;

    public NewWordRecyclerViewAdapter(Context context, List words, int itemId) {
        this.context = context;
        this.itemId = itemId;
        this.words = words;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(itemId, viewGroup, false);
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final MyItemViewHolder itemViewHolder = (MyItemViewHolder) viewHolder;
        itemViewHolder.tvEnglish.setText(words.get(i).getWenglish());
        itemViewHolder.tvChinese.setText(words.get(i).getWchinese());
        //删除单词
        itemViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewWordDBHelper newWordDBHelper =new NewWordDBHelper(context,"tbl_newWord.db",1);
                SQLiteDatabase database = newWordDBHelper.getWritableDatabase();
                int row2 = database.delete("TBL_NEWWORD", "WENGLISH=?", new String[]{words.get(i).getWenglish()});
                Log.e("删除了", words.get(i).getWenglish()+"即"+row2 + "条数据");
                words.remove(i);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        if ( words != null) {
            return  words.size();
        }
        return 0;
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEnglish;
        public TextView tvChinese;
        public ImageView ivDelete;

        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEnglish = itemView.findViewById(R.id.tv_neworwrongenglish);
            tvChinese = itemView.findViewById(R.id.tv_neworwrongchinese);
            ivDelete = itemView.findViewById(R.id.iv_neworwrongimg);

        }
    }


}
