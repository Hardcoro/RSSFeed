package android.rss.model;

import android.os.Handler;

public interface Task {

    String DATA_KEY = "DATA_KEY";

    void start(Handler handler);

    void stop();
}
