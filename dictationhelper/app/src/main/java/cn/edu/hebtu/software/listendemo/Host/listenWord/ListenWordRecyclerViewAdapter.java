package cn.edu.hebtu.software.listendemo.Host.listenWord;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.KeyboardView;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.BlurTransformation;
import cn.edu.hebtu.software.listendemo.Untils.DIYKeyboardView;
import cn.edu.hebtu.software.listendemo.Untils.KeyboardUtil;
import cn.edu.hebtu.software.listendemo.Untils.ReadManager;

public class ListenWordRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Word> listenWords;
    private int itemId;
    private KeyboardView keyboard;


    public ListenWordRecyclerViewAdapter(Context context, List listenWords, int itemId,KeyboardView keyboard) {
        this.context = context;
        this.listenWords = listenWords;
        this.itemId = itemId;
        this.keyboard=keyboard;

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


        itemViewHolder.tvWordChinese.setText(listenWords.get(i).getWchinese().toString());
        itemViewHolder.tvSum.setText(listenWords.size() + "");
        itemViewHolder.tvCurrent.setText(i + 1 + "");
        itemViewHolder.etWord.setText("");
        //用来初始化我们的软键盘
        int type = itemViewHolder.etWord.getInputType();
        new KeyboardUtil(keyboard, itemViewHolder.etWord).showKeyboard();
        //new DIYKeyboardView(context,null).draw(new Canvas());


        //new KeyboardUtil(itemViewHolder.keyboard, itemViewHolder.etWord).showKeyboard();
        //光标问题
        itemViewHolder.etWord.setInputType(type);
        itemViewHolder.etWord.setSelection(itemViewHolder.etWord.getText().toString().length());
       /* itemViewHolder.etWord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (itemViewHolder.etWord.hasFocus()) {
                    //用来初始化我们的软键盘
                    int type = itemViewHolder.etWord.getInputType();
                    new KeyboardUtil(keyboard, itemViewHolder.etWord).showKeyboard();
                    //new KeyboardUtil(itemViewHolder.keyboard, itemViewHolder.etWord).showKeyboard();
                    //光标问题
                    itemViewHolder.etWord.setInputType(type);
                    itemViewHolder.etWord.setSelection(itemViewHolder.etWord.getText().toString().length());
                }
                return false;
            }
        });*/
        RequestOptions options = new RequestOptions();
        options.skipMemoryCache(false);
        options.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        options.bitmapTransform(new BlurTransformation(5, 4));
        Glide.with(context)
                .load(listenWords.get(i).getWimgPath())
                .thumbnail(0.1f)
                .apply(options)
                .into(itemViewHolder.ivWordImg);


    }

    @Override
    public int getItemCount() {
        if (listenWords != null) {
            return listenWords.size();
        }
        return 0;
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView tvWordChinese;
        public ImageView ivWordImg;
        public EditText etWord;
        public TextView tvCurrent;
        public TextView tvSum;
       // public KeyboardView keyboard;


        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWordChinese = itemView.findViewById(R.id.tv_wordChinese);
            etWord = itemView.findViewById(R.id.et_word);
            ivWordImg = itemView.findViewById(R.id.iv_wordImg);
            root = itemView.findViewById(R.id.ll_above);
            tvCurrent = itemView.findViewById(R.id.tv_current);
            tvSum = itemView.findViewById(R.id.tv_sum);
            //keyboard = (KeyboardView) itemView.findViewById(R.id.kv_keyboard);

        }
    }


}
