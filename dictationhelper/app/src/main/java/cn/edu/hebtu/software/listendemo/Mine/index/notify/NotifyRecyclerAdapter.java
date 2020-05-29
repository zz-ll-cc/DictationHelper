package cn.edu.hebtu.software.listendemo.Mine.index.notify;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Message;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.DateUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NotifyRecyclerAdapter extends RecyclerView.Adapter {

    private List<Message> messageList;
    private Context context;
    private int layout_item_id;
    private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
    private User user;
    private OkHttpClient client = new OkHttpClient();
    public NotifyRecyclerAdapter(List<Message> messageList, Context context, int layout_item_id, User user) {
        this.messageList = messageList;
        this.context = context;
        this.layout_item_id = layout_item_id;
        this.user = user;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(layout_item_id, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        Message message = messageList.get(i);
        if (message.getHits() != -1) {
            holder.ivHit.setVisibility(View.INVISIBLE);
        } else {
            holder.ivHit.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(message.getTitleImage()).into(holder.ivCover);
        holder.tvSubTitle.setText(message.getSubtitle());
        holder.tvTitle.setText(message.getTitle());
        holder.tvType.setText(message.getTypeName());
        // 设置时间
        String nowDate = "";
        try {
            Date creDate = sdf1.parse(message.getCreateTime());
//            Date creDate = new Date(message.getCreateTime());
            nowDate = DateUtil.getShowTime(creDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.tvTime.setText(nowDate);

        holder.rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotifyDetailActivity.class);
                intent.putExtra("url", message.getContent());
                context.startActivity(intent);
                sendItemClick(message.getId());
            }
        });
        holder.ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotifyDetailActivity.class);
                intent.putExtra("url", message.getContent());
                context.startActivity(intent);
                sendItemClick(message.getId());
            }
        });
        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotifyDetailActivity.class);
                intent.putExtra("url", message.getContent());
                context.startActivity(intent);
                sendItemClick(message.getId());
            }
        });
        holder.tvType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotifyDetailActivity.class);
                intent.putExtra("url", message.getContent());
                context.startActivity(intent);
                sendItemClick(message.getId());
            }
        });
        holder.tvSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NotifyDetailActivity.class);
                intent.putExtra("url", message.getContent());
                context.startActivity(intent);
                sendItemClick(message.getId());
            }
        });
    }

    private void sendItemClick(int messageId) {
        FormBody fb = new FormBody.Builder()
                .add("userId", user.getUid() + "")
                .add("messageId", messageId + "").build();
        Request request = new Request.Builder()
                .url(Constant.URL_CLICK_NOTIFY_ITEM)
                .post(fb).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                Map<String,Object> postMap = new HashMap<>();
                postMap.put("result",jsonStr);
                postMap.put("itemId",messageId);
                EventBus.getDefault().post(postMap);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == messageList)
            return 0;
        else
            return messageList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHit;
        TextView tvTime;
        ImageView ivCover;
        RelativeLayout rlClick;
        TextView tvTitle;
        TextView tvSubTitle;
        TextView tvType;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHit = itemView.findViewById(R.id.iv_notify_item_hited);
            tvTime = itemView.findViewById(R.id.tv_notify_item_time);
            ivCover = itemView.findViewById(R.id.iv_notify_item_cover);
            rlClick = itemView.findViewById(R.id.rl_notify_item_click);
            tvTitle = itemView.findViewById(R.id.tv_notify_item_title);
            tvSubTitle = itemView.findViewById(R.id.tv_notify_item_subtitle);
            tvType = itemView.findViewById(R.id.tv_notify_item_type_name);
        }
    }
}
