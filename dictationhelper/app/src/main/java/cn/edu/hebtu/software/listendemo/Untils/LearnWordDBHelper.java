package cn.edu.hebtu.software.listendemo.Untils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LearnWordDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_LEARN = "TABLE_LEARN";
    public LearnWordDBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE_LEARN+"(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "WORDID INTEGER," +
                "BOOKID INTEGER" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
