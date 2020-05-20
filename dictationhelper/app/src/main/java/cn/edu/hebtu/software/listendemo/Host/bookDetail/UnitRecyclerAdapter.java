package cn.edu.hebtu.software.listendemo.Host.bookDetail;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.edu.hebtu.software.listendemo.Entity.ChooseUnitItem;
import cn.edu.hebtu.software.listendemo.Entity.UnLock;
import cn.edu.hebtu.software.listendemo.Entity.Unit;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.learnWord.LearnWordActivity;
import cn.edu.hebtu.software.listendemo.Host.listenWord.CustomDialogListenSelect;
import cn.edu.hebtu.software.listendemo.Host.listenWord.ListenWordActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UnitRecyclerAdapter extends RecyclerView.Adapter {
    private FragmentManager fragmentManager;
    private Activity activity;
    private Context context;
    private int layout_item_id;
    private List<Unit> unitList;
    private CheckBox cbChooseAll;
    private Map<Integer, Boolean> canDownMap = new HashMap<>();
    private Map<Integer, ChooseUnitItem> chooseUnitItemMap = new HashMap<>();
    private List<Word> chooseWords = new ArrayList<>();
    private boolean isChooseAllFromUnit;
    private LinearLayout llRecite;
    private LinearLayout llDictation;
    private User user;
    private int[] colors = {R.color.back1, R.color.back2, R.color.back3, R.color.back4, R.color.back5, R.color.back6};
    private int[] colorUsed;
    private int randStart;
    private OkHttpClient client = new OkHttpClient();

    public UnitRecyclerAdapter(Context context, int layout_item_id, List<Unit> unitList,
                               CheckBox cbChooseAll, LinearLayout llRecite, LinearLayout llDictation,
                               User user, Activity activity, FragmentManager fragmentManager) {
        this.cbChooseAll = cbChooseAll;
        this.context = context;
        this.layout_item_id = layout_item_id;
        this.unitList = unitList;
        this.llRecite = llRecite;
        this.llDictation = llDictation;
        this.user = user;
        this.activity=activity;
        this.fragmentManager=fragmentManager;
        initData();
    }

    private void initData() {
        // 初始化下拉框、选择框
        for (Unit unit : unitList) {
            canDownMap.put(unit.getUnid(), false);
            ChooseUnitItem chooseUnitItem = new ChooseUnitItem();
            chooseUnitItem.setSelected(false);
            chooseUnitItem.setUnit(unit);
            chooseUnitItemMap.put(unit.getUnid(), chooseUnitItem);
        }
        Random random = new Random();
        this.randStart = random.nextInt(6);

    }

    public void changeChooseType() {
        // 判断当前的 chooseUnitItemMap 是否已经被全部选中
        isChooseAllFromUnit = true;
        for (int id : chooseUnitItemMap.keySet()) {
            if (!chooseUnitItemMap.get(id).isSelected()) {
                isChooseAllFromUnit = false;
            }
        }
        if (isChooseAllFromUnit) {
            // 如果遍历过后，发现已经全选，则改变全选按钮状态
            cbChooseAll.setChecked(true);
            cbChooseAll.setText("取消全选");
        } else {
            cbChooseAll.setChecked(false);
            cbChooseAll.setText("选择全部");
        }
    }

    public void changeChooseWord() {
        chooseWords.clear();
        for (int id : chooseUnitItemMap.keySet()) {
            if (chooseUnitItemMap.get(id).isSelected()) {
                // 如果是不需要解锁的直接添加
                if (chooseUnitItemMap.get(id).getUnit().getType() == Unit.TYPE_UNLOCK) {
                    chooseWords.addAll(chooseUnitItemMap.get(id).getUnit().getWords());
                }
                // 如果需要解锁则先判断user是否已解锁
                if (null != user.getUnLockList()) {
                    for (int i = 0; i < user.getUnLockList().size(); i++) {
                        if (user.getUnLockList().get(i).getUnitId() == chooseUnitItemMap.get(id).getUnit().getUnid()) {
                            chooseWords.addAll(chooseUnitItemMap.get(id).getUnit().getWords());
                            break;
                        }
                    }
                }
            }
        }
    }

    public void setCbChooseAllListener() {
        cbChooseAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        // 此时通过点击全选按钮，变为“全选选中”状态
                        for (int id : chooseUnitItemMap.keySet()) {
                            chooseUnitItemMap.get(id).setSelected(true);
                        }
                        cbChooseAll.setText("取消全选");
                        changeChooseWord();
                        notifyDataSetChanged();
                    } else {
                        // 此时通过点击全选按钮，变为“全不选中状态”
                        for (int id : chooseUnitItemMap.keySet()) {
                            chooseUnitItemMap.get(id).setSelected(false);
                        }
                        cbChooseAll.setText("选择全部");
                        changeChooseWord();
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void setStartLearnOrDictateListener() {
        llRecite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseWords.isEmpty()) {
                    Toast.makeText(context, "请选择学习单元", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(context, LearnWordActivity.class);
                    intent.putExtra(Constant.DETAIL_CON_RECITE_OR_DICTATION, new Gson().toJson(chooseWords));
                    context.startActivity(intent);
                }

            }
        });
        llDictation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseWords.isEmpty()) {
                    Toast.makeText(context, "请选择默写单元", Toast.LENGTH_SHORT).show();
                } else {
//                    // 只传递重点单词到背诵
//                    List<Word> dicWord = new ArrayList<>();
//                    for (Word word : chooseWords) {
//                        if (word.getType() == Word.TYPE_KEYNODE)
//                            dicWord.add(word);
//                    }


//                    Intent intent = new Intent(context, ListenWordActivity.class);
//                    intent.putExtra(Constant.DETAIL_CON_RECITE_OR_DICTATION, new Gson().toJson(dicWord));
//                    context.startActivity(intent);

                    CustomDialogListenSelect dialog=new CustomDialogListenSelect(chooseWords,activity);
                    dialog.setCancelable(false);
                    dialog.show(fragmentManager,"listenSelect");
                }
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(layout_item_id, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;
        final Unit unit = unitList.get(i);
        if (unit.getType() == Unit.TYPE_LOCK) {
            myViewHolder.ivClock.setVisibility(View.VISIBLE);
            myViewHolder.ivChoose.setVisibility(View.GONE);
            myViewHolder.tvUnitName.setVisibility(View.GONE);
            myViewHolder.ivShowAll.setVisibility(View.GONE);
            myViewHolder.rvWords.setVisibility(View.GONE);
            initLockItem(myViewHolder, unit, i);
        } else {
            myViewHolder.ivClock.setVisibility(View.GONE);
            myViewHolder.ivChoose.setVisibility(View.VISIBLE);
            myViewHolder.tvUnitName.setVisibility(View.VISIBLE);
            myViewHolder.ivShowAll.setVisibility(View.VISIBLE);
            myViewHolder.rvWords.setVisibility(View.VISIBLE);
            initUnLockItem(myViewHolder, unit, i);
        }
        if (null != user.getUnLockList()) {
            for (int j = 0; j < user.getUnLockList().size(); j++) {
                if (user.getUnLockList().get(j).getUnitId() == unit.getUnid()) {
                    myViewHolder.ivClock.setVisibility(View.GONE);
                    myViewHolder.ivChoose.setVisibility(View.VISIBLE);
                    myViewHolder.tvUnitName.setVisibility(View.VISIBLE);
                    myViewHolder.ivShowAll.setVisibility(View.VISIBLE);
                    myViewHolder.rvWords.setVisibility(View.VISIBLE);
                    initUnLockItem(myViewHolder, unit, i);
                }
            }
        }


    }

    private void initLockItem(MyViewHolder myViewHolder, Unit unit, int i){
        myViewHolder.tvUnit.setText(unit.getUnName());
        myViewHolder.rlDetailDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unit.getCost() < user.getUserCredit()) {
                    // 展示一个dialog
                    AlertDialog.Builder adBuilder = new AlertDialog.Builder(context);
                    adBuilder.setTitle("解锁此单元将消耗"+unit.getCost()+"积分");

                    adBuilder.setPositiveButton("确认解锁", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 选中“确定”按钮，解除绑定
                            tryToUnLockThis(unit);
                        }
                    });
                    adBuilder.setNegativeButton("我再想想", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 选中“取消”按钮，取消界面

                        }
                    });
                    adBuilder.create().show();
                }
                else{
                    Toast.makeText(context,"积分不足，快去赚取积分吧",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void tryToUnLockThis(Unit unit) {
        FormBody fb = new FormBody.Builder().add("userId", user.getUid()+"")
                .add("unitId", unit.getUnid()+"").build();
        Request request = new Request.Builder().url(Constant.URL_USER_UNLOCK_UNIT).post(fb).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonWords = response.body().string();
                Map<String,Object> postMap = new HashMap<>();
                postMap.put("type","fromUnLockUnit");
                postMap.put("result",jsonWords);
                postMap.put("unit",unit);
                if (jsonWords.equals("成功")) {
                    EventBus.getDefault().post(postMap);
                }
            }
        });
    }

    private void initUnLockItem(MyViewHolder myViewHolder, Unit unit, int i) {
        myViewHolder.tvUnit.setText(unit.getUnName());
//        myViewHolder.tvUnitName.setText(unit.getUnTitle());
        List<Word> words = unit.getWords();
        WordRecyclerAdapter adapter = new WordRecyclerAdapter(words, context, R.layout.fragment_book_detail_word_item);
        myViewHolder.rvWords.setAdapter(adapter);
        //解决冲突
        myViewHolder.rvWords.setNestedScrollingEnabled(false);
        myViewHolder.rvWords.setFocusableInTouchMode(false);
        myViewHolder.rvWords.requestFocus();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);   // 默认设置垂直布局
        myViewHolder.rvWords.setLayoutManager(layoutManager);
        if (canDownMap.get(unit.getUnid())) {
            myViewHolder.rvWords.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.rvWords.setVisibility(View.GONE);
        }

        if (chooseUnitItemMap.get(unit.getUnid()).isSelected()) {
            myViewHolder.ivChoose.setImageDrawable(context.getResources().getDrawable(R.drawable.choosed));
        } else {
            myViewHolder.ivChoose.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_no));
        }
        // 给控件设置监听器
        myViewHolder.rlUnitBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看此单元是否已经进入被选中的map
                if (chooseUnitItemMap.get(unit.getUnid()).isSelected()) {
                    // 此时说明这个单元为 已经选中 状态
                    chooseUnitItemMap.get(unit.getUnid()).setSelected(false); // 变为未选
                    myViewHolder.ivChoose.setImageDrawable(context.getResources().getDrawable(R.drawable.choose_no));   // 改变选择图标
                    changeChooseType();
                    changeChooseWord();
                    notifyDataSetChanged();
                } else {
                    // 此时这个单元为 未选中 状态
                    chooseUnitItemMap.get(unit.getUnid()).setSelected(true);  // 变为选中状态
                    myViewHolder.ivChoose.setImageDrawable(context.getResources().getDrawable(R.drawable.choosed));
                    changeChooseType();
                    changeChooseWord();
                    notifyDataSetChanged();
                }
            }
        });
        myViewHolder.rlDetailDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看当前是不是展示全部的状态
                // 当目前为查看全部
                if (canDownMap.get(unit.getUnid())) {
                    myViewHolder.ivShowAll.setEnabled(false);
                    canDownMap.put(unit.getUnid(), false);
                    myViewHolder.ivShowAll.setEnabled(true);
                } else {
                    myViewHolder.ivShowAll.setEnabled(false);
                    canDownMap.put(unit.getUnid(), true);
                    myViewHolder.ivShowAll.setEnabled(true);
                }
                notifyDataSetChanged();
            }
        });
        if (!canDownMap.get(unit.getUnid())) {
            myViewHolder.tvUnitName.setVisibility(View.INVISIBLE);
            myViewHolder.rlDetailDown.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            myViewHolder.tvUnitName.setVisibility(View.VISIBLE);
            //设置颜色
            if ((randStart + i) >= colors.length) {
                myViewHolder.rlDetailDown.setBackgroundColor(context.getResources().getColor(colors[randStart + i - colors.length]));
                myViewHolder.rvWords.setBackgroundColor(context.getResources().getColor(colors[randStart + i - colors.length]));
            } else {
                myViewHolder.rlDetailDown.setBackgroundColor(context.getResources().getColor(colors[randStart + i]));
                myViewHolder.rvWords.setBackgroundColor(context.getResources().getColor(colors[randStart + i]));
            }
        }
        myViewHolder.ivShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看当前是不是展示全部的状态
                // 当目前为查看全部
                if (canDownMap.get(unit.getUnid())) {
                    myViewHolder.ivShowAll.setEnabled(false);
                    canDownMap.put(unit.getUnid(), false);
                    myViewHolder.ivShowAll.setEnabled(true);
                } else {
                    myViewHolder.ivShowAll.setEnabled(false);
                    canDownMap.put(unit.getUnid(), true);
                    myViewHolder.ivShowAll.setEnabled(true);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (unitList != null) {
            return unitList.size();
        } else
            return 0;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout rlDetailDown;
        public RelativeLayout rlUnitBorder;
        public TextView tvUnit;
        public ImageView ivChoose;
        public TextView tvUnitName;
        public ImageView ivClock;
        public ImageView ivShowAll;
        public RecyclerView rvWords;
        public int randColor;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rlDetailDown = itemView.findViewById(R.id.rl_fragment_book_detail_down);
            rlUnitBorder = itemView.findViewById(R.id.rl_fragment_book_unit_border);
            tvUnit = itemView.findViewById(R.id.tv_fragment_book_detail_unit);
            tvUnitName = itemView.findViewById(R.id.tv_fragment_book_detail_unit_name);
            ivChoose = itemView.findViewById(R.id.iv_fragment_book_detail_choose);
            ivClock = itemView.findViewById(R.id.iv_fragment_book_detail_clock);
            ivShowAll = itemView.findViewById(R.id.iv_fragment_book_detail_show_all_word);
            rvWords = itemView.findViewById(R.id.recv_fragment_book_detail);

        }

        public int getRandColor() {
            return randColor;
        }

        public void setRandColor(int randColor) {
            this.randColor = randColor;
        }
    }
}
