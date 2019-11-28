package cn.edu.hebtu.software.listendemo.Host.bookDetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;

public class WordRecyclerAdapter extends RecyclerView.Adapter {
    private List<Word> words;
    private Context context;
    private int layout_item_id;

    public WordRecyclerAdapter(List<Word> words, Context context, int layout_item_id) {
        this.words = words;
        this.context = context;
        this.layout_item_id = layout_item_id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(layout_item_id, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        myViewHolder.tvEnglish.setText(words.get(i).getWenglish());
        myViewHolder.tvChinese.setText(words.get(i).getWchinese());
    }

    @Override
    public int getItemCount() {
        if (words != null)
            return words.size();
        else
            return 0;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvChinese;
        private TextView tvEnglish;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChinese = itemView.findViewById(R.id.tv_book_detail_word_chinese);
            tvEnglish = itemView.findViewById(R.id.tv_book_detail_word_english);
        }
    }
}
