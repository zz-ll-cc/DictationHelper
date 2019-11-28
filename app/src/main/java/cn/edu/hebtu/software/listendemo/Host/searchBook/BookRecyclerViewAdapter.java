package com.example.dictationprj.Host.searchBook;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dictationprj.Entity.Book;
import com.example.dictationprj.Host.bookDetail.BookDetailActivity;
import com.example.dictationprj.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;


public class BookRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Book> books;
    private int itemId;

    public BookRecyclerViewAdapter(Context context, List books, int itemId) {
        this.context = context;
        this.books =books;
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(itemId,viewGroup,false);

        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        //设置每一项所显示的内容
        final Book book = books.get(i);
        MyItemViewHolder itemViewHolder=(MyItemViewHolder) viewHolder;
        itemViewHolder.tvBook.setText(book.getBname());
        if (null == book.getBimgPath() || !book.getBimgPath().equals("")){
            try {
                URL  url = new URL(book.getBimgPath());
                Glide.with(context).load(url).into(((MyItemViewHolder) viewHolder).ivBook);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        itemViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, BookDetailActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(books!=null){
            return books.size();
        }
        return 0;
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public TextView tvBook;
        public ImageView ivBook;
        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBook=itemView.findViewById(R.id.tv_book);
            ivBook=itemView.findViewById(R.id.iv_book);
            root=itemView.findViewById(R.id.ll_book);

        }
    }



}
