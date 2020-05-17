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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Entity.WrongWord;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.WrongWordDBHelper;

import static cn.edu.hebtu.software.listendemo.Record.index.NewWordActivity.DELETE_FROM_NEW_WORD;
import static cn.edu.hebtu.software.listendemo.Record.index.WrongWordActivity.DELETE_FROM;
import static cn.edu.hebtu.software.listendemo.Record.index.WrongWordActivity.DELETE_FROM_WRONG_WORD;
import static cn.edu.hebtu.software.listendemo.Record.index.WrongWordActivity.DELETE_ITEM_TIME;


public class WrongWordRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Word> words;
    private int itemId;
    private ViewBinderHelper helper = new ViewBinderHelper();
    private ImageView ivEmpty;
    private RelativeLayout rlEmpty;
    private LinearLayout llHave;
    private String addTime;
    private TextView tvCount;

    public WrongWordRecyclerViewAdapter(Context context, List words, int itemId, ImageView ivEmpty, RelativeLayout rlEmpty, LinearLayout llHave, String addTime, TextView tvCount) {
        this.context = context;
        this.itemId = itemId;
        this.words = words;
        this.rlEmpty = rlEmpty;
        this.ivEmpty = ivEmpty;
        this.llHave = llHave;
        this.addTime = addTime;
        this.tvCount = tvCount;
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
                tvCount.setText(words.size() + "");
                if (words.isEmpty()) {
                    Map<String, Object> deleteMap = new HashMap<>();
                    deleteMap.put(DELETE_FROM, DELETE_FROM_WRONG_WORD);
                    deleteMap.put(DELETE_ITEM_TIME, addTime);
                    EventBus.getDefault().post(deleteMap);
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
