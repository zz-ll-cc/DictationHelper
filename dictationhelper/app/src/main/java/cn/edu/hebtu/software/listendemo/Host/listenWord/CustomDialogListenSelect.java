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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.listenResult.ListenResultActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

//自定义的Dialog需要继承DialogFragment
public class CustomDialogListenSelect extends DialogFragment {
    private Activity activity;
    private List<Word> chooseWords;
    public Boolean ALL = false;
    public Boolean IMPORT = true;
    public Boolean ORDER = true;
    public Boolean OUTORDER = false;
    public Boolean APP = true;
    public Boolean PAPER = false;
    private Button btnSelectAll;
    private Button btnSelectImport;
    private Button btnSelectOrder;
    private Button btnSelectOutOrder;
    private Button btnSelectApp;
    private Button btnSelectPaper;
    private Button btnSelectListen;

    public CustomDialogListenSelect() {
    }

    @SuppressLint("ValidFragment")
    public CustomDialogListenSelect(List<Word> chooseWords, Activity activity) {
        this.chooseWords = chooseWords;
        this.activity = activity;
    }

    //重写方法onCreateView
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //根据布局文件通过布局填充器创建view
        View view = inflater.inflate(R.layout.custom_dialog_listen_select, null);
//        //动态设置自定义Dialog的显示内容的宽和高
//        WindowManager m = getActivity().getWindowManager();
//        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
//        android.view.WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();  //获取对话框当前的参数值
//        p.height = (int) (d.getHeight());   //高度设置为屏幕的0.3
//        p.width = d.getWidth();    //宽度设置为全屏
//        getDialog().getWindow().setAttributes(p);     //设置生效
//        Log.e("tt1", p.height + ":" + p.width);

        //获取布局文件的控件
        CustomDialogListener listener = new CustomDialogListener();
        btnSelectAll = view.findViewById(R.id.btn_listen_select_all);
        btnSelectAll.setOnClickListener(listener);
        btnSelectImport = view.findViewById(R.id.btn_listen_select_import);
        btnSelectImport.setOnClickListener(listener);
        btnSelectOrder = view.findViewById(R.id.btn_listen_select_order);
        btnSelectOrder.setOnClickListener(listener);
        btnSelectOutOrder = view.findViewById(R.id.btn_listen_select_outOrder);
        btnSelectOutOrder.setOnClickListener(listener);
        btnSelectApp = view.findViewById(R.id.btn_listen_select_app);
        btnSelectApp.setOnClickListener(listener);
        btnSelectPaper = view.findViewById(R.id.btn_listen_select_paper);
        btnSelectPaper.setOnClickListener(listener);
        btnSelectListen = view.findViewById(R.id.btn_listen_select_listen);
        btnSelectListen.setOnClickListener(listener);
        //返回view
        return view;
    }

    private class CustomDialogListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_listen_select_all:
                    btnSelectAll.setBackground(getResources().getDrawable(R.drawable.btn_listen_select));
                    btnSelectImport.setBackground(getResources().getDrawable(R.drawable.btn_listen_select_normal));
                    ALL = true;
                    IMPORT = false;
                    break;
                case R.id.btn_listen_select_import:
                    btnSelectImport.setBackground(getResources().getDrawable(R.drawable.btn_listen_select));
                    btnSelectAll.setBackground(getResources().getDrawable(R.drawable.btn_listen_select_normal));
                    IMPORT = true;
                    ALL = false;
                    break;
                case R.id.btn_listen_select_order:
                    btnSelectOrder.setBackground(getResources().getDrawable(R.drawable.btn_listen_select));
                    btnSelectOutOrder.setBackground(getResources().getDrawable(R.drawable.btn_listen_select_normal));
                    ORDER = true;
                    OUTORDER = false;
                    break;
                case R.id.btn_listen_select_outOrder:
                    btnSelectOutOrder.setBackground(getResources().getDrawable(R.drawable.btn_listen_select));
                    btnSelectOrder.setBackground(getResources().getDrawable(R.drawable.btn_listen_select_normal));
                    OUTORDER = true;
                    ORDER = false;
                    break;
                case R.id.btn_listen_select_app:
                    btnSelectApp.setBackground(getResources().getDrawable(R.drawable.btn_listen_select));
                    btnSelectPaper.setBackground(getResources().getDrawable(R.drawable.btn_listen_select_normal));
                    APP = true;
                    PAPER = false;
                    break;
                case R.id.btn_listen_select_paper:
                    btnSelectPaper.setBackground(getResources().getDrawable(R.drawable.btn_listen_select));
                    btnSelectApp.setBackground(getResources().getDrawable(R.drawable.btn_listen_select_normal));
                    PAPER = true;
                    APP = false;
                    break;
                case R.id.btn_listen_select_listen:
                    List<Word> listenWord = new ArrayList<>();
                    Log.e("ttttttt", "ALL=" + ALL + " IMPORT=" + IMPORT + " ORDER=" + ORDER + " OUTORDER=" + OUTORDER + " APP=" + APP + " PAPER=" + PAPER);
                    if (IMPORT == true) {
                        // 只传递重点单词到背诵
                        for (Word word : chooseWords) {
                            if (word.getType() == Word.TYPE_KEYNODE)
                                listenWord.add(word);
                        }
                    } else {
                        // 传递全部单词到背诵
                        for (Word word : chooseWords) {
                            listenWord.add(word);
                        }
                    }
                    if (ORDER == true) {

                    } else {
                        Collections.shuffle(listenWord);
                    }
                    if (APP == true) {
                        Intent intent = new Intent(activity, ListenWordActivity.class);
                        intent.putExtra(Constant.DETAIL_CON_RECITE_OR_DICTATION, new Gson().toJson(listenWord));
                        activity.startActivity(intent);
                        dismiss();
                    } else {
                        Log.e("tttttttt","纸质听写");
                        Intent intent = new Intent(activity, ListenWordPaperActivity.class);
                        intent.putExtra(Constant.DETAIL_CON_RECITE_OR_DICTATION, new Gson().toJson(listenWord));
                        activity.startActivity(intent);
                        dismiss();
                    }
                    break;
            }
        }
    }
}
