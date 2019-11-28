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

import cn.edu.hebtu.software.listendemo.Entity.Version;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;


public class VersionRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Version> visions;
    private int itemId;
    private TextView tvAllVersion;

    public VersionRecyclerViewAdapter(Context context, List visions, int itemId,TextView tvAllVersion) {
        this.context = context;
        this.visions=visions;
        this.itemId = itemId;
        this.tvAllVersion=tvAllVersion;
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
        itemViewHolder.tvVersion.setText(visions.get(i).getBvName());
        if((i+"").equals(getmPosition())){
            itemViewHolder.tvVersion.setTextColor(Color.RED);
        }else {
            itemViewHolder.tvVersion.setTextColor(Color.BLACK);
        }
        itemViewHolder.tvVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAllVersion.setTextColor(Color.BLACK);
                HashMap<String,Object> map=new HashMap<>();
                map.put("tag", Constant.VERSION);
                map.put("m",visions.get(i).getBvId()+"");
                map.put("position",i+"");
                EventBus.getDefault().postSticky(new Gson().toJson(map));
            }
        });
        tvAllVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAllVersion.setTextColor(Color.RED);
                HashMap<String,Object> map=new HashMap<>();
                map.put("tag", Constant.VERSION);
                map.put("m",Constant.VERSION_ALL+"");
                map.put("position",-1+"");
                EventBus.getDefault().postSticky(new Gson().toJson(map));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(visions!=null){
            return visions.size();
        }
        return 0;
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout root;
        public TextView tvVersion;
        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVersion=itemView.findViewById(R.id.tv_gv);
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
