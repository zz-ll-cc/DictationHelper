package cn.edu.hebtu.software.listendemo.Host.index;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Book;
import cn.edu.hebtu.software.listendemo.Host.searchBook.SearchBookActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class HostFragment extends Fragment {
    private LinearLayout llFindBooks;
    private LinearLayout llContinueStudy;
    private List<Book> res = new ArrayList<>();
    private HostRecyclerAdapter adapter = null;
    private RecyclerView recyclerView = null;
    private SharedPreferences sp = null;
    private Gson gson = new GsonBuilder().serializeNulls().create();
    private View view;
    private static final int GET_BOOKS = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_BOOKS:
                    initView(view);
                    setListener();
                    break;
            }
        }
    };
    private OkHttpClient client = new OkHttpClient();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_host, container, false);
        initData();
        return view;
    }

    private void setListener() {
        llFindBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchBookActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recv_fragment_host);
        adapter = new HostRecyclerAdapter(R.layout.fragment_host_recycler_item, res, getContext());
        recyclerView.setAdapter(adapter);
        llContinueStudy = view.findViewById(R.id.ll_fragment_host_continueStudy);
        llFindBooks = view.findViewById(R.id.ll_fragment_host_findBooks);
    }

    private void initData() {
        sp = getContext().getSharedPreferences(Constant.SP_NAME, MODE_PRIVATE);
        FormBody fb = new FormBody.Builder().build();
        Request request = new Request.Builder().url(Constant.URL_BOOKS_FIND_ALL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonBooks = response.body().string();
                Message message = new Message();
                message.what = GET_BOOKS;
                Type type = new TypeToken<List<Book>>() {
                }.getType();
                res = gson.fromJson(jsonBooks, type);
                handler.sendMessage(message);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initView(view);
    }
}
