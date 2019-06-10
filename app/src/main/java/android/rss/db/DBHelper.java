package android.rss.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "RssDb";

    public static final String TABLE_RSS_FEED = "RssFeedTable";

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "rssFeed";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_RSS_FEED + "(" + KEY_ID
                + " integer primary key autoincrement," + KEY_NAME + " text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_RSS_FEED);

        onCreate(db);
    }
}

