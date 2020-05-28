package cn.edu.hebtu.software.listendemo.Mine.index.cardbag;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.edu.hebtu.software.listendemo.Entity.Inventory;
import cn.edu.hebtu.software.listendemo.Entity.Item;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CardBagDetailRecyclerAdapter extends RecyclerView.Adapter {
    private List<Inventory> inventories;
    private Context context;
    private int layout_item_id;
    private User user;
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new GsonBuilder().serializeNulls().create();


    public CardBagDetailRecyclerAdapter(User user, List<Inventory> inventories, Context context, int layout_item_id) {
        this.inventories = inventories;
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
        Inventory inventory = inventories.get(i);
        Item item = inventory.getItem();
        holder.tvCardTagName.setText(inventory.getName());
        llbackground(holder,inventory);
        if (null != item) {
            holder.tvCardBagItemDescripe.setText(item.getDescription());
        }
            Log.e("ttttttttttk", inventory.toString());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = format.format(Calendar.getInstance().getTime());
            try {
                Date nowDate = format.parse(time);
                String expendTime = inventory.getExpiryTime();
                String[] arr = expendTime.split("T");
                String[] tarr = arr[1].split(".000");
                String dd = arr[0] + " " + tarr[0];
                Date expiryDate = format.parse(dd);
                if (inventory.getIsUsed() == 0) {
                    if (nowDate.getTime() > expiryDate.getTime()) {
                        holder.llBackground.setBackground(context.getResources().getDrawable(R.drawable.coupon_gray));
                        holder.tvCreditBagUse.setText("已过期");
                    }
                    if(nowDate.getTime() < expiryDate.getTime()) {
                        llbackground(holder,inventory);
                        holder.tvCreditBagUse.setText("去使用");
                    }
                    holder.tvCardBagExpirationName.setText("过期时间");
                    holder.tvCardBagExpirationTime.setText(dd);
                }
                if (inventory.getIsUsed() == 1) {
                    //holder.llBackground.setBackground(context.getResources().getDrawable(R.drawable.coupon1));
                    holder.tvCardBagExpirationName.setText("过期时间");
                    holder.tvCreditBagUse.setText("已使用");
//                    String dateExpend = inventory.getExpendTime();
//                    String[] arrExpend = dateExpend.split("T");
//                    String[] tarrExpend = arrExpend[1].split(".000");
//                    String ddExpend = arrExpend[0] + " " + tarrExpend[0];
                    holder.tvCardBagExpirationTime.setText(dd);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        //}
        holder.tvCreditBagUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tvCreditBagUse.getText().equals("去使用")) {
                    AlertDialog.Builder adBuilder = new AlertDialog.Builder(context);
                    adBuilder.setIcon(R.drawable.cardbag);
                    adBuilder.setTitle("卡卷包");
                    adBuilder.setMessage("是否使用该" + inventory.getName() + "(" + item.getItemType().getDescription() + ")");
                    adBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            useInventory(inventory.getId());
                        }
                    });
                    adBuilder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    adBuilder.create().show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != inventories) {
            return inventories.size();
        } else {
            return 0;
        }
    }

    public void changeDataSource(List<Inventory> inventories) {
        this.inventories = inventories;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llBackground;
        TextView tvCardTagName;
        TextView tvCardBagItemDescripe;
        TextView tvCreditBagUse;
        TextView tvCardBagExpirationTime;
        TextView tvCardBagExpirationName;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            llBackground = itemView.findViewById(R.id.ll_background);
            tvCardTagName = itemView.findViewById(R.id.tv_card_bag_item_name);
            tvCardBagItemDescripe = itemView.findViewById(R.id.tv_card_bag_item_descripe);
            tvCreditBagUse = itemView.findViewById(R.id.tv_credit_bag_usetv_credit_bag_use);
            tvCardBagExpirationTime = itemView.findViewById(R.id.tv_card_bag_expiration_time);
            tvCardBagExpirationName = itemView.findViewById(R.id.tv_card_bag_expiration_name);

        }
    }

    /**
     * 获取十六进制的颜色代码.例如  "#6E36B4" , For HTML ,
     *
     * @return String
     */
    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        return r + g + b;
    }

    private void useInventory(int inventoryId) {
        FormBody fb = new FormBody.Builder().add("userId", user.getUid() + "")
                .add("inventoryId", inventoryId + "").build();
        Request request = new Request.Builder().url(Constant.URL_USE_INVENTORY).post(fb).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String inventoryJson = response.body().string();
                Log.e("inventoryJson", inventoryJson);
                if (inventoryJson != null) {
                    Inventory inventory = gson.fromJson(inventoryJson + "", Inventory.class);
                    Map<String, Object> postMap = new HashMap<>();
                    postMap.put("type", "cardbaguse");
                    postMap.put("result", inventoryJson);
                    postMap.put("inventoryId", inventoryId + "");
                    EventBus.getDefault().post(postMap);
                }
            }
        });
    }

    public void llbackground(MyViewHolder holder,Inventory inventory){
        switch (inventory.getItemId()) {
            case 1:
                holder.llBackground.setBackground(context.getResources().getDrawable(R.drawable.coupon1));
                holder.tvCardTagName.setTextColor(context.getResources().getColor(R.color.coupon1));
                break;
            case 2:
                holder.llBackground.setBackground(context.getResources().getDrawable(R.drawable.coupon3));
                holder.tvCardTagName.setTextColor(context.getResources().getColor(R.color.coupon3));
                break;
            case 3:
                holder.llBackground.setBackground(context.getResources().getDrawable(R.drawable.coupon4));
                holder.tvCardTagName.setTextColor(context.getResources().getColor(R.color.coupon4));
                break;
            case 4:
                holder.llBackground.setBackground(context.getResources().getDrawable(R.drawable.coupon5));
                holder.tvCardTagName.setTextColor(context.getResources().getColor(R.color.coupon5));
                break;
        }
    }
}
