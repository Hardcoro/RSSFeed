package android.rss.model.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.rss.db.DBHelper;
import android.rss.model.RssFeedModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class SaveRssTask implements Task {

    private Thread task;

    private DBHelper dbHelper;

    private Gson gson;

    public SaveRssTask(Context context) {
        gson = new GsonBuilder().create();
        dbHelper = new DBHelper(context);
    }

    @Override
    public void start(final Object data, Handler handler) {
        task = new Thread("saveRssTask") {

            @Override
            public void run() {
                RssFeedModel rssFeedModel = (RssFeedModel) data;
                String json = gson.toJson(rssFeedModel, RssFeedModel.class);

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, json);

                database.insert(DBHelper.TABLE_RSS_FEED, null, contentValues);

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
