package cn.edu.hebtu.software.listendemo.Untils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SearchHistoryDBHelper extends SQLiteOpenHelper {
    public static final String TBL_HISTORY = "TBL_BOOK";

    public SearchHistoryDBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBookSql = "CREATE TABLE " + TBL_HISTORY + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +   //  id
                "english TEXT," +                           // 英文
                "chinese TEXT," +                           // 中文
                "wid INTEGER)";                             // word 表中 id
        db.execSQL(createBookSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
