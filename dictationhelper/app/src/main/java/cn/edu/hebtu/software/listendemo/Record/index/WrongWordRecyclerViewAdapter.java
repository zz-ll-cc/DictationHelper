package cn.edu.hebtu.software.listendemo.Record.index;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.WrongWord;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.WrongWordDBHelper;


public class WrongWordRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<WrongWord> words;
    private int itemId;
    private ViewBinderHelper helper = new ViewBinderHelper();
    private ImageView ivEmpty;
    private RelativeLayout rlEmpty;
    private LinearLayout llHave;

    public WrongWordRecyclerViewAdapter(Context context, List words, int itemId, ImageView ivEmpty, RelativeLayout rlEmpty, LinearLayout llHave) {
        this.context = context;
        this.itemId = itemId;
        this.words = words;
        this.rlEmpty = rlEmpty;
        this.ivEmpty = ivEmpty;
        this.llHave = llHave;
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

        helper.bind(itemViewHolder.srlAll, words.get(i).getWchinese());
        itemViewHolder.tvEnglish.setText(words.get(i).getWenglish());
        itemViewHolder.tvChinese.setText(words.get(i).getWchinese());
        //删除单词
        itemViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WrongWordDBHelper wrongWordDBHelper = new WrongWordDBHelper(context, "tbl_wrongWord.db", 1);
                SQLiteDatabase database = wrongWordDBHelper.getWritableDatabase();
                int row2 = database.delete("TBL_WRONGWORD", "WENGLISH=?", new String[]{words.get(i).getWenglish()});
                Log.e("删除了", words.get(i).getWenglish() + "即" + row2 + "条数据");
                words.remove(i);
                if (words.isEmpty()){
                    rlEmpty.setVisibility(View.VISIBLE);
                    ivEmpty.setImageResource(R.drawable.empty_wrong);
                    rlEmpty.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    llHave.setVisibility(View.GONE);
                }else{
                    rlEmpty.setVisibility(View.GONE);
                    llHave.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        });
        helper.setOpenOnlyOne(true);
    }

    @Override
    public int getItemCount() {
        if (words != null) {
            return words.size();
        }
        return 0;
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEnglish;
        public TextView tvChinese;
        public ImageView ivDelete;
        public SwipeRevealLayout srlAll;

        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEnglish = itemView.findViewById(R.id.tv_neworwrongenglish);
            tvChinese = itemView.findViewById(R.id.tv_neworwrongchinese);
            ivDelete = itemView.findViewById(R.id.iv_neworwrongimg);
            srlAll = itemView.findViewById(R.id.srl_word);
        }
    }


}
