package cn.edu.hebtu.software.listendemo.Mine.index.shopping;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.Item;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.edu.hebtu.software.listendemo.Untils.Constant.TYPE_BUY_TYPE_1;
import static cn.edu.hebtu.software.listendemo.Untils.Constant.URL_BUY_SHOP_ITEM;

public class ShowBuyDialog {
    private static ShowBuyDialog showBuyDialog;

    private ShowBuyDialog() {
    }

    public static ShowBuyDialog getDialog() {
        if (null == showBuyDialog) {
            synchronized (ShowBuyDialog.class) {
                if (null == showBuyDialog) {
                    showBuyDialog = new ShowBuyDialog();
                }
            }
        }
        return showBuyDialog;
    }

    private Context context;
    private Item item;
    private Dialog dialog;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void showDialog() {
        //1、使用Dialog、设置style
        dialog = new Dialog(context, R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(context, R.layout.custom_dialog_buy_shop_item, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.CENTER);
        //设置弹出动画
        window.setWindowAnimations(R.style.shop_item_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView ivCover = dialog.findViewById(R.id.iv_buy_item_cover);
        TextView tvName = dialog.findViewById(R.id.tv_buy_item_name);
        TextView tvDescription = dialog.findViewById(R.id.tv_buy_item_description);
        TextView tvLeft = dialog.findViewById(R.id.tv_buy_item_left);
        TextView tvMyCredit = dialog.findViewById(R.id.tv_buy_item_my_credit);
        RelativeLayout rlBuy = dialog.findViewById(R.id.rl_shopping_item_buy);
        TextView tvCost = dialog.findViewById(R.id.tv_shop_item_cost);

        Glide.with(context).load(item.getCover()).into(ivCover);
        tvName.setText(item.getName());
        tvDescription.setText(item.getItemType().getDescription());
        if (item.getId() == 0)
            tvLeft.setText(item.getLeft() + " 剩余");
        else{
            tvLeft.setText(item.getQuantity()+" 剩余");
        }
        tvMyCredit.setText("积分余额：" + user.getUserCredit());
        tvCost.setText(item.getPrice() + " 积分兑换");
        rlBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getUserCredit() >= item.getPrice()) {
                    buyItem(user.getUid(), item.getId(), item.getPrice());
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "积分不够哦~", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    // 购买商品
    private void buyItem(int userId, int itemId, int price) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder()
                .add("userId", userId + "")
                .add("itemId", itemId + "")
                .add("price", price + "");
        FormBody body = builder.build();
        Request request = new Request.Builder().post(body).url(URL_BUY_SHOP_ITEM).build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
//                Toast.makeText(context,"update失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String strings = response.body().string();
                Map<String, Object> buyMap = new HashMap<>();
                buyMap.put("buyType", strings);
                if (strings.equals(TYPE_BUY_TYPE_1)) {
                    buyMap.put("item", item);
                }
                EventBus.getDefault().post(buyMap);
            }
        });
    }
}
