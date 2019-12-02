package cn.edu.hebtu.software.listendemo.Host.index;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Book;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.bookDetail.BookDetailActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;


public class HostRecyclerAdapter extends RecyclerView.Adapter {
    private int layout_item_id;
    private List<Book> res = null;
    private List<Book> orginalRes = null;
    private Context context;
    private int bindId;
    private List<Integer> collectRes = new ArrayList<>();
    private SharedPreferences sp;
    private Gson gson = new Gson();

    public HostRecyclerAdapter(int layout_item_id, List<Book> res, Context context) {
        this.layout_item_id = layout_item_id;
        this.res = res;
        this.context = context;
        this.orginalRes = res;
        sp = context.getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        changeRes();
    }

    private void changeRes() {
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        collectRes = gson.fromJson(sp.getString(Constant.COLLECT_KEY, Constant.DEFAULT_COLLECT_LIST), type);
        bindId = sp.getInt(Constant.BIND_KEY, Constant.DEFAULT_BIND_ID);
        if (bindId != -1 && collectRes.size() == 0) {
            // 此时已经有绑定教材，但无收藏教材
            for (int i = 0; i < res.size(); i++) {
                // 将绑定教材放入第一个位置
                if (res.get(i).getBid() == bindId) {
                    Book book = res.get(i);
                    res.remove(i);
                    res.add(0, book);
                    break;
                }
            }
        } else if (bindId != -1 && collectRes.size() != 0) {
            // 此时既有绑定教材，也有收藏教材
            // 将每个收藏的教材放到前面
            for (int i = 0; i < collectRes.size(); i++) {
                int id = collectRes.get(i);
                for (int j = 0; j < res.size(); j++) {
                    if (res.get(j).getBid() == id) {
                        res.add(1, res.get(j));
                        res.remove(j + 1);
                        break;
                    }
                }
            }
            for (int i = 0; i < res.size(); i++) {
                // 先将绑定教材放入第一个位置
                if (res.get(i).getBid() == bindId) {
                    Book book = res.get(i);
                    res.add(0, book);
                    res.remove(i + 1);
                    break;
                }
            }
        } else if (bindId == -1 && collectRes.size() != 0) {
            // 此时无绑定教材，有收藏教材
            // 将每个收藏的教材放到前面
            for (int i = 0; i < collectRes.size(); i++) {
                int id = collectRes.get(i);
                for (int j = 0; j < res.size(); j++) {
                    if (res.get(j).getBid() == id) {
                        res.add(0, res.get(j));
                        res.remove(j + 1);
                        break;
                    }
                }
            }
        } else {
            // 此时既无绑定，也无收藏，直接放着即可
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(layout_item_id, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        MyViewHolder viewHolder1 = (MyViewHolder) viewHolder;
        final Book book = res.get(i);
        viewHolder1.tvName.setText(book.getBname());
        // TODO: 2019/11/28 设置图片
        if (null == book.getBimgPath() || !book.getBimgPath().equals("")){
            try {
                URL url = new URL(book.getBimgPath());
                Glide.with(context).load(url).into(viewHolder1.ivCover);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        if (bindId == book.getBid()) {
            viewHolder1.ivCollect.setVisibility(View.GONE);
            viewHolder1.ivBind.setVisibility(View.VISIBLE);
            viewHolder1.ivBind.setImageDrawable(context.getResources().getDrawable(R.drawable.binded));
        } else if (collectRes.contains(book.getBid())) {
            viewHolder1.ivBind.setVisibility(View.GONE);
            viewHolder1.ivCollect.setVisibility(View.VISIBLE);
            viewHolder1.ivCollect.setImageDrawable(context.getResources().getDrawable(R.drawable.collected));
        } else {
            viewHolder1.ivBind.setVisibility(View.GONE);
            viewHolder1.ivCollect.setVisibility(View.VISIBLE);
            viewHolder1.ivCollect.setImageDrawable(context.getResources().getDrawable(R.drawable.collect_no));
        }

        // 设置监听器
        viewHolder1.llOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constant.HOST_CON_DETAIL_BOOK, res.get(i));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        viewHolder1.ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果是未收藏，点击可进行收藏
                // 如果已经收藏，点击可进行取消收藏
                // 1. 判断当前内容是否在收藏中
                if (collectRes.contains(book.getBid())) {
                    // 此时已经在收藏夹
                    for (int i = 0; i < collectRes.size(); i++) {
                        if (collectRes.get(i) == book.getBid()) {
                            collectRes.remove(i);
                            break;
                        }
                    }
                } else {
                    // 此时不在收藏夹
                    collectRes.add(book.getBid());
                }
                // 2. 将新的数据放入SharedP
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constant.COLLECT_KEY, gson.toJson(collectRes));
                editor.commit();
                // 3. 修改显示样式
                changeRes();
            }
        });
        viewHolder1.ivBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击取消绑定
                // 显示一个Dialog
                AlertDialog.Builder adBuilder = new AlertDialog.Builder(context);
                adBuilder.setTitle("确定解除绑定");

                adBuilder.setPositiveButton("确认解除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 选中“确定”按钮，解除绑定
                        // 更改SharedP中数据
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(Constant.BIND_KEY, -1);
                        editor.commit();
                        // 修改显示样式
                        changeRes();
                        Toast.makeText(context, "已解除绑定教材，请进入教材详情进行绑定", Toast.LENGTH_SHORT).show();
                    }
                });
                adBuilder.setNegativeButton("取消解除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 选中“取消”按钮，取消界面

                    }
                });
                adBuilder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != res) {
            return res.size();
        } else
            return 0;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCover;
        public ImageView ivCollect;
        public ImageView ivBind;
        public TextView tvName;
        public LinearLayout llOut;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_host_rec_item_cover);
            ivBind = itemView.findViewById(R.id.iv_host_rec_item_bind);
            ivCollect = itemView.findViewById(R.id.iv_host_rec_item_collect);
            tvName = itemView.findViewById(R.id.tv_host_rec_item_name);
            llOut = itemView.findViewById(R.id.ll_fragment_host_out);
        }
    }
}
