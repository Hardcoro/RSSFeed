package android.rss.model.task;

import android.content.Context;
import android.os.Handler;
import android.rss.db.DBHelper;

@Deprecated
public class CreateDBTask implements Task {

    private Thread createDbTask;

    private DBHelper dbHelper;

    private Context context;

    public CreateDBTask(Context context) {
        this.context = context;
    }

    @Override
    public void start(Handler handler) {
        createDbTask = new Thread("createDbTask") {

            @Override
            public void run() {
                dbHelper = new DBHelper(context);

                dbHelper.getWritableDatabase();

                dbHelper.close();
            }
        };

        createDbTask.start();
    }

    @Override
    public void stop() {
        createDbTask.interrupt();
    }
}
