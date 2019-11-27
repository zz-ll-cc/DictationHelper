package com.example.dictationprj.Host;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.dictationprj.Book;
import com.example.dictationprj.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HostFragment extends Fragment {
    private LinearLayout llFindBooks;
    private LinearLayout llContinueStudy;
    private List<Book> res = new ArrayList<>();
    private HostRecyclerAdapter adapter = null;
    private RecyclerView recyclerView = null;
    private SharedPreferences sp = null;
    private Gson gson = new Gson();
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_host,container,false);
        initData();
        initView(view);
        setListener();
        return view;
    }
    private void setListener() {
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recv_fragment_host);
        adapter = new HostRecyclerAdapter(R.layout.fragment_host_recycler_item , res , getContext());
        recyclerView.setAdapter(adapter);
        llContinueStudy = view.findViewById(R.id.ll_fragment_host_continueStudy);
        llFindBooks = view.findViewById(R.id.ll_fragment_host_findBooks);
    }

    private void initData() {
        sp = getContext().getSharedPreferences("用户",MODE_PRIVATE);
        Book book1 = new Book();
        book1.setId(0);
        book1.setName("三上0");
        book1.setCover("");
        Book book2 = new Book();
        book2.setId(1);
        book2.setName("四上1");
        book2.setCover("");
        Book book3 = new Book();
        book3.setId(2);
        book3.setName("三下2");
        book3.setCover("");
        Book book4 = new Book();
        book4.setId(3);
        book4.setName("五上3");
        book4.setCover("");
        Book book5 = new Book();
        book5.setId(4);
        book5.setName("四下4");
        book5.setCover("");
        res.add(book1);res.add(book2);res.add(book3);res.add(book4);res.add(book5);


        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("bind",1);
        List<Integer> collectedId = new ArrayList<>();
        collectedId.add(0);
        collectedId.add(3);
        String colStr = gson.toJson(collectedId);
        editor.putString("collectList",colStr);
        editor.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        initView(view);
    }
}
