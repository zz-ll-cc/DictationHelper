package cn.edu.hebtu.software.listendemo.Host.searchBook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Book;
import cn.edu.hebtu.software.listendemo.Host.bookDetail.BookDetailActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;


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
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.HOST_CON_DETAIL_BOOK, books.get(i));
                intent.putExtras(bundle);
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
