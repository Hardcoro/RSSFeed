package android.rss.model.task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.rss.db.DBHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ReadRssTask implements Task {

    private Thread task;

    private DBHelper dbHelper;

    private Gson gson;

    public ReadRssTask(Context context) {
        gson = new GsonBuilder().create();
        dbHelper = new DBHelper(context);
    }

    @Override
    public void start(final Object data, Handler handler) {
        task = new Thread("readRssTask") {

            @Override
            public void run() {
                SQLiteDatabase database = dbHelper.getReadableDatabase();

                Cursor cursor = database.query(DBHelper.TABLE_RSS_FEED, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    do {
                        Log.d("RSS", "ID = " + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex));
                    } while (cursor.moveToNext());
                } else
                    Log.d("RSS FEED","0 rows");

                cursor.close();
                dbHelper.close();
            }
        };

        task.start();
    }

    @Override
    public void stop() {
        task.interrupt();
    }
}
