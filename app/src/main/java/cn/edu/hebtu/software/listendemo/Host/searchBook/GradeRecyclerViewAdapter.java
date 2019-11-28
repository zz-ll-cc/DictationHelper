package cn.edu.hebtu.software.listendemo.Host.searchBook;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Grade;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;


public class GradeRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Grade> grades;
    private int itemId;
    private TextView tvAllGrade;
    public GradeRecyclerViewAdapter(Context context, List grades, int itemId , TextView tvAllGrade) {
        this.context = context;
        this.grades =grades;
        this.itemId = itemId;
        this.tvAllGrade = tvAllGrade;
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
        final MyItemViewHolder itemViewHolder=(MyItemViewHolder) viewHolder;
        itemViewHolder.tvGrade.setText(grades.get(i).getGname());
        if((i+"").equals(getmPosition())){
            itemViewHolder.tvGrade.setTextColor(Color.RED);
        }else {
            itemViewHolder.tvGrade.setTextColor(Color.BLACK);
        }
        itemViewHolder.tvGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAllGrade.setTextColor(Color.BLACK);
                HashMap<String,Object> map=new HashMap<>();
                map.put("tag", Constant.GRADE);
                map.put("m",grades.get(i).getGid()+"");
                map.put("position",i+"");
                EventBus.getDefault().postSticky(new Gson().toJson(map));
            }
        });
        tvAllGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAllGrade.setTextColor(Color.RED);
                HashMap<String,Object> map=new HashMap<>();
                map.put("tag", Constant.GRADE);
                map.put("m",Constant.GRADE_ALL+"");
                map.put("position",-1+"");
                EventBus.getDefault().postSticky(new Gson().toJson(map));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(grades!=null){
            return grades.size();
        }
        return 0;
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public TextView tvGrade;
        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGrade=itemView.findViewById(R.id.tv_gv);
            root=itemView.findViewById(R.id.ll_gv);

        }
    }

    private  String mPosition="-1";

    public String getmPosition() {
        return mPosition;
    }

    public void setmPosition(String mPosition) {
        this.mPosition = mPosition;
    }
}
