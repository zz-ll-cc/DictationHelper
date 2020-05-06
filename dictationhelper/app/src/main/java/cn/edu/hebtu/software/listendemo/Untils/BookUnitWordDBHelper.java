package cn.edu.hebtu.software.listendemo.Untils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建Book ，Unit ，Word 数据表，取值时先取sqlite中数据，并比对数据库中数据；若数据库中数据发生改变则更新
 */
public class BookUnitWordDBHelper extends SQLiteOpenHelper {
    public static final String TBL_BOOK = "TBL_BOOK";
    public static final String TBL_UNIT = "TBL_UNIT";
    public static final String TBL_WORD = "TBL_WORD";

    public BookUnitWordDBHelper(Context context, String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createBookSql = "CREATE TABLE " + TBL_BOOK + " (" +
                "bid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "gid INTEGER," +
                "bvid INTEGER," +
                "bname TEXT,bimgPath TEXT," +
                "bunitAccount INTEGER," +
                "bookWordVersion INTEGER," +
                "deleted INTEGER," +
                "version INTEGER," +
                "createTime TEXT," +
                "updateTime TEXT)";
        String createWordSql = "CREATE TABLE " + TBL_WORD + " (" +
                "wid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bid INTEGER,unid INTEGER,type INTEGER," +
                "wimgPath TEXT,wchinese TEXT,wenglish TEXT," +
                "deleted INTEGER," +
                "version INTEGER," +
                "createTime TEXT," +
                "updateTime TEXT)";
        String createUnitSql = "CREATE TABLE " + TBL_UNIT + " (" +
                "unid INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bid INTEGER,type INTEGER," +
                "unName TEXT,unTitle TEXT," +
                "deleted INTEGER," +
                "version INTEGER," +
                "createTime TEXT," +
                "updateTime TEXT)";
        db.execSQL(createBookSql);
        db.execSQL(createUnitSql);
        db.execSQL(createWordSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
