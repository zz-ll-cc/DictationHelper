package cn.edu.hebtu.software.listendemo.Host.searchBook;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Book;
import cn.edu.hebtu.software.listendemo.Entity.Grade;
import cn.edu.hebtu.software.listendemo.Entity.Version;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SearchBookActivity extends AppCompatActivity {

    private String gid= Constant.GRADE_ALL+"";
    private String bvid=Constant.VERSION_ALL+"";
    private GradeRecyclerViewAdapter gradeRecyclerViewAdapter;
    private VersionRecyclerViewAdapter versionRecyclerViewAdapter;
    private BookRecyclerViewAdapter bookRecyclerViewAdapter;
    private RecyclerView recyclerViewGrade;
    private RecyclerView recyclerViewVersion;
    private RecyclerView recyclerViewBook;
    private OkHttpClient client = new OkHttpClient();
    private List<Grade> gradeList;
    private List<Version>  versionList;
    private List<Book>  bookList;
    private Gson gson = new Gson();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initGradeView();
                    break;
                case 2:
                    initVersionView();
                    break;
                case 3:
                    initBookView();
                    break;
            }
        }
    };

    private TextView tvAllGrade;
    private TextView tvAllVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        EventBus.getDefault().register(this);
        initData();
        initBook(gid,bvid);
    }
    // 实现消息处理方法
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMsgReceived(String e){
        HashMap<String,Object> map=new HashMap<>();
        Type type = new TypeToken<HashMap<String,Object>>() {}.getType();
        map=new Gson().fromJson(e,type);
        if(map.get("tag").equals(Constant.GRADE)){
            gradeRecyclerViewAdapter.setmPosition(map.get("position").toString());
            gid=map.get("m").toString();
            initBook(gid,bvid);
            gradeRecyclerViewAdapter.notifyDataSetChanged();
        }
        if(map.get("tag").equals(Constant.VERSION)){
            versionRecyclerViewAdapter.setmPosition(map.get("position").toString());
            bvid=map.get("m").toString();
            initBook(gid,bvid);
            versionRecyclerViewAdapter.notifyDataSetChanged();
        }
        bookRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void initGradeView(){
        tvAllGrade = findViewById(R.id.tv_search_book_all_grade);
        recyclerViewGrade=findViewById(R.id.rv_grade);
        gradeRecyclerViewAdapter=new GradeRecyclerViewAdapter(this,gradeList,R.layout.activity_grade_version_recycler_item,tvAllGrade);
        recyclerViewGrade.setAdapter(gradeRecyclerViewAdapter);//设置适配器
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewGrade.setLayoutManager(layoutManager);//必须调用，设置布局管理器
    }

    private void initVersionView(){
        tvAllVersion = findViewById(R.id.tv_search_book_all_version);
        recyclerViewVersion=findViewById(R.id.rv_version);
        versionRecyclerViewAdapter=new VersionRecyclerViewAdapter(this,versionList,R.layout.activity_grade_version_recycler_item,tvAllVersion);
        recyclerViewVersion.setAdapter(versionRecyclerViewAdapter);
        RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewVersion.setLayoutManager(layoutManager1);
    }

    private void initBookView(){
        recyclerViewBook=findViewById(R.id.rv_book);
        bookRecyclerViewAdapter=new BookRecyclerViewAdapter(this,bookList,R.layout.activity_book_recycler_item);
        recyclerViewBook.setAdapter(bookRecyclerViewAdapter);
        recyclerViewBook.setLayoutManager(new GridLayoutManager(this, 3));
    }

    private void initData() {
        FormBody fb = new FormBody.Builder().build();
        Request request = new Request.Builder()
                .url(Constant.URL_GRADES_FIND_ALL)
                .post(fb)
                .build();
        Request request1 = new Request.Builder()
                .url(Constant.URL_VERSIONS_FIND_ALL)
                .post(fb)
                .build();
        Call call = client.newCall(request);
        Call call1 = client.newCall(request1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String getJson = response.body().string();
                Type type = new TypeToken<List<Grade>>() {}.getType();
                gradeList = gson.fromJson(getJson, type);
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        });
        call1.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String getJson = response.body().string();
                Type type = new TypeToken<List<Version>>() {}.getType();
                versionList = gson.fromJson(getJson, type);
                Message message = new Message();
                message.what = 2;
                handler.sendMessage(message);
            }
        });
    }

    public void initBook(String gid,String bvid){
        FormBody fb1 = new FormBody.Builder().add("gid",gid).add("bvid",bvid).build();
        Request request2 = new Request.Builder()
                .url(Constant.URL_BOOKS_FIND_BY_VER_AND_GRA)
                .post(fb1)
                .build();
        Call call2 = client.newCall(request2);
        call2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String getJson = response.body().string();
                Type type = new TypeToken<List<Book>>() {}.getType();
                bookList = gson.fromJson(getJson, type);
                Message message = new Message();
                message.what = 3;
                handler.sendMessage(message);
            }
        });
    }


}
