package android.rss.model.task;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.rss.db.DBHelper;
import android.rss.model.RssFeed;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadRssTask implements Task {

    private Thread task;

    private DBHelper dbHelper;

    private Gson gson;

    public ReadRssTask(Context context) {
        gson = new GsonBuilder().create();
        dbHelper = new DBHelper(context);
    }

    @Override
    public void start(final Object data, final Handler handler) {
        task = new Thread("readRssTask") {

            @Override
            public void run() {
                List<RssFeed> rssFeedList = new ArrayList<>();

                SQLiteDatabase database = dbHelper.getReadableDatabase();

                Cursor cursor = database.query(DBHelper.TABLE_RSS_FEED, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    do {
                        String rssJson = cursor.getString(nameIndex);
                        Log.d("RSS", "ID = " + cursor.getInt(idIndex) + ", name = " + rssJson);

                        rssFeedList.addAll(Arrays.asList(gson.fromJson(rssJson, RssFeed[].class)));

                    } while (cursor.moveToNext());
                } else {
                    Log.d("RSS FEED", "0 rows");
                }

                cursor.close();
                dbHelper.close();

                Bundle bundle = new Bundle();
                bundle.putSerializable(DATA_KEY, (Serializable) rssFeedList);

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
