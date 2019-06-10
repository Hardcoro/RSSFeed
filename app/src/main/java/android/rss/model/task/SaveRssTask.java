package android.rss.model.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.rss.db.DBHelper;
import android.rss.model.RssFeed;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class SaveRssTask implements Task {

    private Thread task;

    private DBHelper dbHelper;

    private Gson gson;

    public SaveRssTask(Context context) {
        gson = new GsonBuilder().create();
        dbHelper = new DBHelper(context);
    }

    @Override
    public void start(final Object data, final Handler handler) {
        task = new Thread("saveRssTask") {

            @Override
            public void run() {
                List<RssFeed> rssList = (List<RssFeed>) data;
                String json = gson.toJson(rssList.toArray(), RssFeed[].class);

                SQLiteDatabase database = dbHelper.getWritableDatabase();

                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, json);

                int deleteResult = database.delete(DBHelper.TABLE_RSS_FEED, null, null);
                Log.d("RSS", "SaveRssTask delete result = " + deleteResult);

                long rowId = database.insert(DBHelper.TABLE_RSS_FEED, null, contentValues);
                Log.d("RSS", "SaveRssTask rowId = " + rowId);

                dbHelper.close();

                Bundle bundle = new Bundle();
                bundle.putLong(DATA_KEY, rowId);

                Message message = new Message();
                message.setData(bundle);

                handler.sendMessage(message);
            }
        };

        task.start();
    }

    @Override
    public void stop() {
        if (task != null) {
            task.interrupt();
        }
    }
}
