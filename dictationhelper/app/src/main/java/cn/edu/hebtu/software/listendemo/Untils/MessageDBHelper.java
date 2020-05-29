package cn.edu.hebtu.software.listendemo.Untils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_MESSAGE = "MESSAGE";

    public MessageDBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE MESSAGE (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TITLEIMAGE TEXT," +
                "TITLE TEXT," +
                "SUBTITLE TEXT," +
                "CONTENT TEXT," +
                "TYPENAME TEXT," +
                "HITS INTEGER," +
                "DELETED INTEGER," +
                "CREATETIME TEXT,UPDATETIME TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
