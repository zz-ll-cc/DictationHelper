package cn.edu.hebtu.software.listendemo.Host.learnWord;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.ReadManager;

public class LearnWordRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Word> learnWords;
    private int itemId;
    private ReadManager readManager;
    private SQLiteDatabase database;

    public LearnWordRecyclerViewAdapter(Context context, List learnWords, int itemId, SQLiteDatabase database) {
        this.context = context;
        this.learnWords = learnWords;
        this.itemId = itemId;
        readManager = new ReadManager(context, "");
        this.database = database;
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
        final MyItemViewHolder itemViewHolder = (MyItemViewHolder) viewHolder;

        itemViewHolder.tvWordChinese.setText(learnWords.get(i).getWchinese().toString());
        itemViewHolder.tvWordEnglish.setText(learnWords.get(i).getWenglish().toString());
        itemViewHolder.tvSum.setText(learnWords.size() + "");
        itemViewHolder.tvCurrent.setText(i + 1 + "");
        itemViewHolder.ivTrumpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readManager.pronounce(learnWords.get(i).getWenglish());
            }
        });
        RequestOptions options = new RequestOptions()//.placeholder(placeholder == 0 ? R.drawable.img_loading : placeholder)
                .skipMemoryCache(false)  //用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存所有图片(原图,转换图)
                .fitCenter()   //fitCenter 缩放图片充满ImageView CenterInside大缩小原(图) CenterCrop大裁小扩充满ImageView  Center大裁(中间)小原
                .error(R.drawable.error);
        Glide.with(context).load(learnWords.get(i).getWimgPath()).apply(options).thumbnail(Glide.with(context).load(R.drawable.wait2)).into(itemViewHolder.ivWordImg);
       // Glide.with(context).load(learnWords.get(i).getWimgPath()).into(itemViewHolder.ivWordImg);

        final Word w = learnWords.get(i);
        //添加生词
        itemViewHolder.ivAddNewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int num = 0;
                Cursor cursor = database.query("TBL_NEWWORD", null, "WENGLISH=?", new String[]{w.getWenglish()}, null, null, null);
                if (cursor.getCount() == 0) {
                    ContentValues word = new ContentValues();
                    word.put("WENGLISH", w.getWenglish());
                    word.put("WCHINESE", w.getWchinese());
                    word.put("WIMGPATH", w.getWimgPath());
                    word.put("UNID", w.getUnid());
                    word.put("BID", w.getBid());
                    word.put("TYPE", w.getType());
                    word.put("ISTRUE", w.getIsTrue());
                    word.put("ADDTIME",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                    long row = database.insert("TBL_NEWWORD", null, word);
                    Log.e("插入生词的行号", row + "");
                    Toast.makeText(context, "添加成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "已添加不要重复添加", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (learnWords != null) {
            return learnWords.size();
        }
        return 0;
    }


    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView tvWordChinese;
        public TextView tvWordEnglish;
        public ImageView ivWordImg;
        public TextView tvCurrent;
        public TextView tvSum;
        public ImageView ivTrumpet;
        public ImageView ivAddNewWord;

        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWordChinese = itemView.findViewById(R.id.tv_wordChinese);
            tvWordEnglish = itemView.findViewById(R.id.tv_wordEnglish);
            ivWordImg = itemView.findViewById(R.id.iv_wordImg);
            root = itemView.findViewById(R.id.ll_above);
            tvCurrent = itemView.findViewById(R.id.tv_current);
            tvSum = itemView.findViewById(R.id.tv_sum);
            ivTrumpet = itemView.findViewById(R.id.iv_trumpet);
            ivAddNewWord = itemView.findViewById(R.id.iv_addNewWord);
        }
    }


}
