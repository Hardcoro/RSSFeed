package android.rss.model.task;

import android.os.Handler;

public interface Task {

    String DATA_KEY = "DATA_KEY";

    void start(Handler handler);

    void stop();
}
