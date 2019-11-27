package cn.edu.hebtu.software.listendemo.Host.searchBook;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.R;

public class VersionRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Map<String,Object>> visions;
    private int itemId;

    public VersionRecyclerViewAdapter(Context context, List visions, int itemId) {
        this.context = context;
        this.visions=visions;
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
        MyItemViewHolder itemViewHolder=(MyItemViewHolder) viewHolder;
        itemViewHolder.tvVersion.setText(visions.get(i).get("version").toString());

        //设置每一项的点击事件监听器
        itemViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击第"+i+"条数据",Toast.LENGTH_LONG).show();
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


}
