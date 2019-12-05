package cn.edu.hebtu.software.listendemo.Record.index;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.edu.hebtu.software.listendemo.R;

public class RecordFragment extends Fragment {
    private LinearLayout llnew;
    private LinearLayout llwrong;
    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_record,container,false);
        return view;
    }

    @Override
    public void onResume() {
        initView(view);
        //生词本跳转
        llnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), NewWordActivity.class);
                startActivity(intent);
            }
        });
        //错词本跳转
        llwrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), WrongWordActivity.class);
                startActivity(intent);
            }
        });
        super.onResume();
    }
    private void initView(View view) {
        llnew=view.findViewById(R.id.ll_new);
        llwrong=view.findViewById(R.id.ll_wrong);
    }



}
