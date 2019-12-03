package cn.edu.hebtu.software.listendemo.Host.bookDetail;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.Entity.ChooseUnitItem;
import cn.edu.hebtu.software.listendemo.Entity.Unit;
import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.learnWord.LearnWordActivity;
import cn.edu.hebtu.software.listendemo.Host.listenWord.ListenWordActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

public class UnitRecyclerAdapter extends RecyclerView.Adapter {
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

    public UnitRecyclerAdapter(Context context, int layout_item_id, List<Unit> unitList,
                               CheckBox cbChooseAll, LinearLayout llRecite, LinearLayout llDictation) {
        this.cbChooseAll = cbChooseAll;
        this.context = context;
        this.layout_item_id = layout_item_id;
        this.unitList = unitList;
        this.llRecite = llRecite;
        this.llDictation = llDictation;
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
                chooseWords.addAll(chooseUnitItemMap.get(id).getUnit().getWords());
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
                    Intent intent = new Intent(context, ListenWordActivity.class);
                    intent.putExtra(Constant.DETAIL_CON_RECITE_OR_DICTATION, new Gson().toJson(chooseWords));
                    context.startActivity(intent);
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
        myViewHolder.tvUnit.setText(unit.getUnName());
        myViewHolder.tvUnitName.setText(unit.getUnTitle());
        List<Word> words = unit.getWords();
        WordRecyclerAdapter adapter = new WordRecyclerAdapter(words, context, R.layout.fragment_book_detail_word_item);
        myViewHolder.rvWords.setAdapter(adapter);
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
                    ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(
                            myViewHolder.ivShowAll, "rotationX", 180.0f, 360.0f
                    );
                    rotateAnimator.setDuration(500);
                    rotateAnimator.start();
                    myViewHolder.ivShowAll.setEnabled(true);
                } else {
                    myViewHolder.ivShowAll.setEnabled(false);
                    canDownMap.put(unit.getUnid(), true);
                    ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(
                            myViewHolder.ivShowAll, "rotationX", 0.0f, 180.0f
                    );
                    rotateAnimator.setDuration(500);
                    rotateAnimator.start();
                    myViewHolder.ivShowAll.setEnabled(true);
                }
                notifyDataSetChanged();
            }
        });
        myViewHolder.ivShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看当前是不是展示全部的状态
                // 当目前为查看全部
                if (canDownMap.get(unit.getUnid())) {
                    myViewHolder.ivShowAll.setEnabled(false);
                    canDownMap.put(unit.getUnid(), false);
                    ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(
                            myViewHolder.ivShowAll, "rotationX", 180.0f, 360.0f
                    );
                    rotateAnimator.setDuration(500);
                    rotateAnimator.start();
                    myViewHolder.ivShowAll.setEnabled(true);
                } else {
                    myViewHolder.ivShowAll.setEnabled(false);
                    canDownMap.put(unit.getUnid(), true);
                    ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(
                            myViewHolder.ivShowAll, "rotationX", 0.0f, 180.0f
                    );
                    rotateAnimator.setDuration(500);
                    rotateAnimator.start();
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
    }
}
