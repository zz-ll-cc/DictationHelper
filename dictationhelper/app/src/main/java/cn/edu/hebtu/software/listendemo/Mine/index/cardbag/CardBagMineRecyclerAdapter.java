package cn.edu.hebtu.software.listendemo.Mine.index.cardbag;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Inventory;
import cn.edu.hebtu.software.listendemo.Entity.Item;
import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.Unit;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Mine.index.credit.CustomDialogSignSuccess;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.SP_NAME;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.USER_KEEP_KEY;

public class CardBagMineRecyclerAdapter extends RecyclerView.Adapter {
    private List<Inventory> inventories;
    private Context context;
    private int layout_item_id;
    private User user;
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new GsonBuilder().serializeNulls().create();

    public CardBagMineRecyclerAdapter(User user, List<Inventory> inventories, Context context, int layout_item_id) {
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
        if (item != null) {
            holder.tvCardBagItemDescripe.setText(item.getItemType().getDescription());
        }
        String date = inventory.getExpiryTime();
        String[] arr = date.split("T");
        String[] tarr = arr[1].split(".000");
        String dd = arr[0] + " " + tarr[0];
        holder.tvCardBagExpirationTime.setText(dd);
        holder.tvCreditBagUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adBuilder = new AlertDialog.Builder(context);
                adBuilder.setIcon(R.drawable.cardbag);
                adBuilder.setTitle("卡卷包");
                adBuilder.setMessage("是否使用该"+inventory.getName()+"("+item.getItemType().getDescription()+")");
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
        });
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
                Log.e("inventoryJson",inventoryJson);
                if (inventoryJson != null) {
                    Inventory inventory = gson.fromJson(inventoryJson + "", Inventory.class);
                    Map<String,Object> postMap = new HashMap<>();
                    postMap.put("type","cardbaguse");
                    postMap.put("result",inventoryJson);
                    postMap.put("inventoryId",inventoryId+"");
                    EventBus.getDefault().post(postMap);
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llBackground;
        TextView tvCardTagName;
        TextView tvCardBagItemDescripe;
        TextView tvCreditBagUse;
        TextView tvCardBagExpirationTime;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            llBackground = itemView.findViewById(R.id.ll_background);
            tvCardTagName = itemView.findViewById(R.id.tv_card_bag_item_name);
            tvCardBagItemDescripe = itemView.findViewById(R.id.tv_card_bag_item_descripe);
            tvCreditBagUse = itemView.findViewById(R.id.tv_credit_bag_usetv_credit_bag_use);
            tvCardBagExpirationTime = itemView.findViewById(R.id.tv_card_bag_expiration_time);

        }
    }

    public void llbackground(MyViewHolder holder, Inventory inventory){
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
