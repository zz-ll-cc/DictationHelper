package cn.edu.hebtu.software.listendemo.Host.listenResult;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import cn.edu.hebtu.software.listendemo.Entity.Word;
import cn.edu.hebtu.software.listendemo.Host.index.ListenIndexActivity;
import cn.edu.hebtu.software.listendemo.R;
import cn.edu.hebtu.software.listendemo.Untils.Constant;

public class ListenResultActivity extends AppCompatActivity {
    private ListenResultRecyclerViewAdapter listenResultSuccessRecyclerViewAdapter;
    private ListenResultRecyclerViewAdapter listenResultMineRecyclerViewAdapter;
    private RecyclerView recyclerViewListenSuccess;
    private RecyclerView recyclerViewListenMine;
    private List<Word>  successList;
    private List<Word>  mineList;
    private TextView tvReturnHost;
    private  TextView tvReturnBookDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen_result);
        initData();
        initView();
    }

    private void initView(){
        //返回到主页
        tvReturnHost=findViewById(R.id.tv_returnHost);
        tvReturnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ListenResultActivity.this, ListenIndexActivity.class);
                startActivity(intent);
            }
        });
        //再听写一次
        tvReturnBookDetail=findViewById(R.id.tv_returnBookDetail);
        tvReturnBookDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerViewListenSuccess=findViewById(R.id.rv_success);
        recyclerViewListenMine=findViewById(R.id.rv_mine);
        float sum=successList.size();
        int error=0;
        for(int i=0;i<successList.size();i++){
            successList.get(i).setIsTrue(Constant.SPELL_TRUE);
            if(mineList.get(i).getWenglish().equals(successList.get(i).getWenglish())){
                mineList.get(i).setIsTrue(Constant.SPELL_TRUE);
            }else{
                mineList.get(i).setIsTrue(Constant.SPELL_FALSE);
                error++;
            }
        }
        double score=(sum-error)*(1.0)/sum * 100;
        final LeanTextView mText = findViewById(R.id.lean);
        mText.setText(Html.fromHtml("<u>"+(int)score+"</u>"));
        mText.setmDegrees(20);
        listenResultSuccessRecyclerViewAdapter=new ListenResultRecyclerViewAdapter(this,successList,R.layout.activity_grade_version_recycler_item);
        listenResultMineRecyclerViewAdapter=new ListenResultRecyclerViewAdapter(this,mineList,R.layout.activity_grade_version_recycler_item);

        //设置适配器
        recyclerViewListenSuccess.setAdapter(listenResultSuccessRecyclerViewAdapter);
        recyclerViewListenMine.setAdapter(listenResultMineRecyclerViewAdapter);
        //必须调用，设置布局管理器
        recyclerViewListenSuccess.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewListenMine.setLayoutManager(new GridLayoutManager(this, 2));


    }


    private void initData() {
        Intent intent=getIntent();
        String str=intent.getStringExtra("success");
        String str1=intent.getStringExtra("mine");
        Type listType=new TypeToken< List<Word> >(){}.getType();
        if(str!=null && !str.equals("")){
            successList=new Gson().fromJson(str,listType);
        }
        if(str1!=null && !str1.equals("")){
            mineList=new Gson().fromJson(str1,listType);
        }
    }
}
