package cn.edu.hebtu.software.listendemo.Host.listenWord;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.listenResult.ListenResultActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

//自定义的Dialog需要继承DialogFragment
public class CustomDialogListenWord extends DialogFragment {
    private List<Word> listenWordlist;
    private List<Word> mineWordlist;
    private Activity activity;
    public CustomDialogListenWord() {
    }

    @SuppressLint("ValidFragment")
    public CustomDialogListenWord(List<Word> listenWordlist, List<Word> mineWordlist ,Activity activity) {
       this.listenWordlist=listenWordlist;
       this.mineWordlist=mineWordlist;
       this.activity = activity;
    }

    //重写方法onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //根据布局文件通过布局填充器创建view
        View view = inflater.inflate(R.layout.custom_dialog_finishlisten, null);
       /* //动态设置自定义Dialog的显示内容的宽和高
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        android.view.WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();  //获取对话框当前的参数值
        Log.e("tt1",p.height+":"+p.width);
        p.height = (int) (d.getHeight() * 0.3);   //高度设置为屏幕的0.3
        p.width = d.getWidth();    //宽度设置为全屏
        getDialog().getWindow().setAttributes(p);     //设置生效
        Log.e("tt1",p.height+":"+p.width);*/

        //获取布局文件的控件
        TextView btnOK = view.findViewById(R.id.btn_OK_listen);
        TextView btnCancel = view.findViewById(R.id.btn_cancel_listen);

        //给按钮添加自定义的监听器
        CustomDialogListener listener = new CustomDialogListener();
        btnOK.setOnClickListener(listener);
        btnCancel.setOnClickListener(listener);

        //返回view
        return view;
    }

    private class CustomDialogListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_OK_listen:
                    Intent intent =new Intent(getActivity(), ListenResultActivity.class);
                    intent.putExtra("success",new Gson().toJson(listenWordlist));
                    intent.putExtra("mine",new Gson().toJson(mineWordlist));
                    startActivity(intent);
                    activity.finish();
                    break;
                case R.id.btn_cancel_listen:
                    //让当前Dialog消失
                    getDialog().dismiss();
                    break;
            }
        }
    }
}
