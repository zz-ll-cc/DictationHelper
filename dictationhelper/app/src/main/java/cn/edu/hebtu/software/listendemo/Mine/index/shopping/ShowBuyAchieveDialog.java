package cn.edu.hebtu.software.listendemo.Mine.index.shopping;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.edu.hebtu.software.listendemo.Entity.Item;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Mine.index.cardbag.CreditBagActivity;
import cn.edu.hebtu.software.listendemo.R;

public class ShowBuyAchieveDialog {
    private static ShowBuyAchieveDialog achieveDialog;

    private ShowBuyAchieveDialog() {
    }

    public static ShowBuyAchieveDialog getDialog() {
        if (null == achieveDialog) {
            synchronized (ShowBuyDialog.class) {
                if (null == achieveDialog) {
                    achieveDialog = new ShowBuyAchieveDialog();
                }
            }
        }
        return achieveDialog;
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
        View view = View.inflate(context, R.layout.custom_dialog_buy_achieve, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        ImageView ivCover = dialog.findViewById(R.id.iv_buy_achieve_cover);
        TextView tvName = dialog.findViewById(R.id.tv_buy_achieve_name);
        TextView tvTypeDes = dialog.findViewById(R.id.tv_buy_achieve_type_name_dura);
        Button btnUse = dialog.findViewById(R.id.btn_buy_achieve_to_use);
        TextView tvClose = dialog.findViewById(R.id.tv_buy_achieve_close);

        Glide.with(context).load(item.getCover()).into(ivCover);
        tvName.setText("《" + item.getName() + "》");
        long duration = item.getItemType().getDurationTime() / (3600 * 24);
        tvTypeDes.setText(item.getItemType().getName() + "X 1 (" + duration + "天有效)");
        btnUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo:跳转至卡券
                Intent intent = new Intent(context, CreditBagActivity.class);
                context.startActivity(intent);
                dialog.dismiss();
            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
