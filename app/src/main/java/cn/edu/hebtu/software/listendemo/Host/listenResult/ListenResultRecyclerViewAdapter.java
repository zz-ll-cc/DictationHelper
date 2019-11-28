package cn.edu.hebtu.software.listendemo.Host.listenResult;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

public class ListenResultRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Word> listenWord;
    private int itemId;

    public ListenResultRecyclerViewAdapter(Context context, List listenWord, int itemId) {
        this.context = context;
        this.listenWord =listenWord;
        this.itemId = itemId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(itemId,viewGroup,false);

        return new MyItemViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        //设置每一项所显示的内容
        MyItemViewHolder itemViewHolder=(MyItemViewHolder) viewHolder;
        if(listenWord.get(i).getIsTrue()== Constant.SPELL_FALSE){
            itemViewHolder.tvWord.setTextColor(context.getResources().getColor(R.color.red));
        }
        if(listenWord.get(i).getWenglish().equals("")){
            itemViewHolder.tvWord.setText(Constant.NO_STYLE);
        }else{
            itemViewHolder.tvWord.setText(listenWord.get(i).getWenglish());
        }

    }

    @Override
    public int getItemCount() {
        if(listenWord!=null){
            return listenWord.size();
        }
        return 0;
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public TextView tvWord;
        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWord=itemView.findViewById(R.id.tv_gv);
            root=itemView.findViewById(R.id.ll_gv);

        }
    }



}
