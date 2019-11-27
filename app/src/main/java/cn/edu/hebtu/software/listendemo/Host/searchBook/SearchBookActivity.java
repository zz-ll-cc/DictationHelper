package cn.edu.hebtu.software.listendemo.Host.searchBook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.hebtu.software.listendemo.R;

public class SearchBookActivity extends AppCompatActivity {

    private GradeRecyclerViewAdapter gradeRecyclerViewAdapter;
    private VersionRecyclerViewAdapter versionRecyclerViewAdapter;
    private BookRecyclerViewAdapter bookRecyclerViewAdapter;
    private RecyclerView recyclerViewGrade;
    private RecyclerView recyclerViewVersion;
    private RecyclerView recyclerViewBook;
    private List<Map<String,Object>>  gradeList;
    private List<Map<String,Object>>  versionList;
    private List<Map<String,Object>>  bookList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);
        initData();
        initView();
    }

    private void initView(){
        recyclerViewGrade=findViewById(R.id.rv_grade);
        recyclerViewVersion=findViewById(R.id.rv_version);
        recyclerViewBook=findViewById(R.id.rv_book);

        gradeRecyclerViewAdapter=new GradeRecyclerViewAdapter(this,gradeList,R.layout.activity_grade_version_recycler_item);
        versionRecyclerViewAdapter=new VersionRecyclerViewAdapter(this,versionList,R.layout.activity_grade_version_recycler_item);
        bookRecyclerViewAdapter=new BookRecyclerViewAdapter(this,bookList,R.layout.activity_book_recycler_item);

        //设置适配器
        recyclerViewGrade.setAdapter(gradeRecyclerViewAdapter);
        recyclerViewVersion.setAdapter(versionRecyclerViewAdapter);
        recyclerViewBook.setAdapter(bookRecyclerViewAdapter);
        //必须调用，设置布局管理器
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewGrade.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager layoutManager1=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewVersion.setLayoutManager(layoutManager1);
        //RecyclerView.LayoutManager layoutManager2=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        //recyclerViewBook.setLayoutManager(layoutManager2);
        recyclerViewBook.setLayoutManager(new GridLayoutManager(this, 3));
    }


    private void initData() {
        String[] grades={"一年级上册","一年级下册","二年级上册","二年级下册","三年级上册","三年级下册"};
        String[] versions={"冀教版","人教版","北师大版"};
        String[] names={"冀教版一年级上册","冀教版一年级下册","冀教版二年级上册","冀教版二年级下册","冀教版三年级上册","冀教版三年级下册"};
        long[] gid={0,1,2,3,4,5};  long[] vid={0,1,2};long[] nid={0,1,2,3,4,5};
        gradeList=new ArrayList<>();
        versionList=new ArrayList<>();
        bookList=new ArrayList<>();
        for(int i=0;i<grades.length;++i){
            Map<String,Object> map=new HashMap<>();
            map.put("grade",grades[i]);
            map.put("id",gid[i]);
            gradeList.add(map);
        }
        for(int i=0;i<versions.length;++i){
            Map<String,Object> map=new HashMap<>();
            map.put("version",versions[i]);
            map.put("id",vid[i]);
            versionList.add(map);
        }
        for(int i=0;i<names.length;++i){
            Map<String,Object> map=new HashMap<>();
            map.put("bname",names[i]);
            map.put("id",nid[i]);
            bookList.add(map);
        }
    }
}
