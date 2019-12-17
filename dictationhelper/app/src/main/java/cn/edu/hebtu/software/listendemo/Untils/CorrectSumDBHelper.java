package cn.edu.hebtu.software.listendemo.Untils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CorrectSumDBHelper extends SQLiteOpenHelper {

    public CorrectSumDBHelper(Context context, String name, int version) {
        super(context, name,null, version);
    }

    //第一次打开数据回调
    @Override
    public void onCreate(SQLiteDatabase db) {
        //初始化数据库操作。并且是第一次打开数据库时调用
        String createSQL="CREATE TABLE TBL_CURRECTSUM(" +
                "SID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "SUM INT,ADDTIME DATE)";
        db.execSQL(createSQL);
        Log.e("correctsumHelper","onCreate");
    }

    //更新数据库版本
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
