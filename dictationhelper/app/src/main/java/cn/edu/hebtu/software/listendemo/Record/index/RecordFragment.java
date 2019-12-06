package cn.edu.hebtu.software.listendemo.Record.index;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.hebtu.software.listendemo.Entity.EventInfo;
import cn.edu.hebtu.software.listendemo.Entity.User;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;
import cn.edu.hebtu.software.listendemo.Untils.SmoothScrollLayoutManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class RecordFragment extends Fragment {
    private LinearLayout llnew;
    private LinearLayout llwrong;
    @BindView(R.id.rv_statistics)
    RecyclerView rvStatistics;
    private ImageView imageView;
    //传过来图片的url
    private List<String> urlList;
    private StatisticAdapter adapter;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap)msg.obj;
            imageView.setImageBitmap(bitmap);//将图片的流转换成图片
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receive(EventInfo<String,String,String> eventInfo){
        urlList = eventInfo.getContentList();
        initAdapter();
        initRecycler();
    }

    private View view;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_record,container,false);
        imageView=view.findViewById(R.id.iv);
        ButterKnife.bind(this,view);
        SharedPreferences sp = getActivity().getSharedPreferences(Constant.SP_NAME,MODE_PRIVATE);
        User user = new Gson().fromJson(sp.getString(Constant.USER_KEEP_KEY,Constant.DEFAULT_KEEP_USER),User.class);
        OkHttpClient okHttpClient=new OkHttpClient();
        FormBody fb = new FormBody.Builder().add("uid",user.getUid()+"").build();
        Request request = new Request.Builder().url(Constant.URL_GET_RECORD).post(fb).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("tt","请求失败");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                 Log.e("response","请求成功");
                 Log.e("re",request.body()+"");
                //InputStream inputStream = response.body().byteStream();//得到图片的流
                //Log.e("inputStream",inputStream+"");
                //File file=new File();
                //Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                //Log.e("bitmap",bitmap+"");
                //Message msg = new Message();
               // msg.obj = bitmap;
                //handler.sendMessage(msg);
        }
        });

        return view;
    }

    private void initAdapter() {
        adapter = new StatisticAdapter(R.layout.fragment_statistics_detail,urlList,getContext());
    }

    private void initRecycler() {
        SmoothScrollLayoutManager layoutManager = new SmoothScrollLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvStatistics.setAdapter(adapter);
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
