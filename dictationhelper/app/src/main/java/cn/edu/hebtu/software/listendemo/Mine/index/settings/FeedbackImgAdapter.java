package cn.edu.hebtu.software.listendemo.Mine.index.settings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.edu.hebtu.software.listendemo.R;


public class FeedbackImgAdapter extends RecyclerView.Adapter<FeedbackImgAdapter.MyViewHolder> {

    private List<String> mDatas;
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;

    public FeedbackImgAdapter(Context context, List<String> datas) {
        this.mDatas = datas;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_post_activity, parent, false);
        // 获取屏幕宽度
        int widthPixels = mContext.getResources().getDisplayMetrics().widthPixels;
        int width = (widthPixels - 30)/3 -20;
        int height = width;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width,height);
        layoutParams.setMargins(5,5,5,5);
        view.setLayoutParams(layoutParams);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (position >= FeedbackActivity.IMAGE_SIZE) {// 图片已选完时，隐藏添加按钮
            holder.flBorder.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.GONE);
        } else {
            holder.flBorder.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.VISIBLE);
        }
        Glide.with(mContext).load(mDatas.get(position))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flBorder;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_push_speak_img_item);
            flBorder = itemView.findViewById(R.id.fl_push_speak_img_item);
        }
    }


}
