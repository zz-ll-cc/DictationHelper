package cn.edu.hebtu.software.listendemo.Untils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WrongWordDBHelper extends SQLiteOpenHelper {

    public WrongWordDBHelper(Context context, String name, int version) {
        super(context, name,null, version);
    }

    //第一次打开数据回调
    @Override
    public void onCreate(SQLiteDatabase db) {
        //初始化数据库操作。并且是第一次打开数据库时调用
        String createSQL="CREATE TABLE TBL_WRONGWORD(" +
                "WID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "WENGLISH TEXT,WCHINESE TEXT,WIMGPATH TEXT," +
                "UNID INT,BID INT,TYPE INT,ISTRUE INT,ADDTIME DATE)";
        db.execSQL(createSQL);
    }

    //更新数据库版本
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
