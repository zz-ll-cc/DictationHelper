package cn.edu.hebtu.software.listendemo.Host.listenWord;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

public class ListenResultSelectRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Word> paperlist;
    private int itemId;
    private Map<Integer, Boolean> checkStatus;//用来记录所有checkbox的状态

    public ListenResultSelectRecyclerViewAdapter(Context context, List<Word> paperlist, int itemId,Map<Integer, Boolean> checkStatus) {
        this.context = context;
        this.paperlist = paperlist;
        this.itemId = itemId;
        this.checkStatus=checkStatus;
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
        MyItemViewHolder itemViewHolder = (MyItemViewHolder) viewHolder;
        itemViewHolder.tvChinese.setText(paperlist.get(i).getWchinese());
        itemViewHolder.tvEnglish.setText(paperlist.get(i).getWenglish());
        itemViewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int count = group.getChildCount();
                for(int j = 0 ;j < count;j++){
                    RadioButton rb =(RadioButton) group.getChildAt(j);
                    if(rb.isChecked()){
                        if (rb.getText().equals("对")){
                            checkStatus.put(i,true);
                            notifyDataSetChanged();
                        }
                        if (rb.getText().equals("错")){
                            checkStatus.put(i,false);
                            notifyDataSetChanged();
                        }
                        Log.e("tttttttttt",i+" "+rb.getText()+"");
                        break;
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (paperlist != null) {
            return paperlist.size();
        }
        return 0;
    }

    private class MyItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvChinese;
        public TextView tvEnglish;
        public RadioGroup radioGroup;
        public RadioButton rbt;
        public RadioButton rbf;

        public MyItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChinese = itemView.findViewById(R.id.tv_paper_listen_item_chinese);
            tvEnglish = itemView.findViewById(R.id.tv_paper_listen_item_english);
            radioGroup = itemView.findViewById(R.id.rg_paper);
            rbt = itemView.findViewById(R.id.rb_t);
            rbf = itemView.findViewById(R.id.rb_f);
        }
    }


}
