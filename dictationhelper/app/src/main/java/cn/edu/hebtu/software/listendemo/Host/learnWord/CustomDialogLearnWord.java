package cn.edu.hebtu.software.listendemo.Host.learnWord;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.listenWord.ListenWordActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

//自定义的Dialog需要继承DialogFragment
public class CustomDialogLearnWord extends DialogFragment {
    private List<Word> learnWordlist;
    private Activity context;
    public CustomDialogLearnWord() {
    }

    @SuppressLint("ValidFragment")
    public CustomDialogLearnWord(List<Word> learnWordlist, Activity context) {
        this.learnWordlist = learnWordlist;
        this.context = context;
    }

    //重写方法onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //根据布局文件通过布局填充器创建view
        View view = inflater.inflate(R.layout.custom_dialog_finishlearn, null);

        //获取布局文件的控件
        TextView btnOK = view.findViewById(R.id.btn_ok_learn);
        TextView btnCancel = view.findViewById(R.id.btn_cancel_learn);

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
                case R.id.btn_ok_learn:
                    Gson gson = new Gson();
                    Intent intent = new Intent(getActivity(), ListenWordActivity.class);
                    intent.putExtra(Constant.RECITE_CON_DICTATION, gson.toJson(learnWordlist));
                    startActivity(intent);
                    context.finish();
                    break;
                case R.id.btn_cancel_learn:
                    //让当前Dialog消失
                    context.finish();
                    getDialog().dismiss();
                    break;
            }
        }
    }
}
