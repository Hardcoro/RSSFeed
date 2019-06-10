package android.rss.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.rss.model.RssFeed;
import android.rss.model.task.Task;
import android.rss.view.RssFeedView;

import java.util.ArrayList;
import java.util.List;

public class RssFeedPresenterImpl implements RssFeedPresenter {

    // SAMPLE 1: https://news.yahoo.com/rss/entertainment
    // SAMPLE 2: http://feed.androidauthority.com
    // SAMPLE 3: http://blog.aweber.com/feed
    // SAMPLE 4: https://www.nasa.gov/rss/dyn/breaking_news.rss

    private static final long SAVE_TASK_ERROR_CODE = -1;

    private RssFeedView view;

    private Task loadRssTask;
    private Task saveRssTask;
    private Task readRssTask;

    private List<RssFeed> rssList = new ArrayList<>();

    public RssFeedPresenterImpl(RssFeedView view, Task loadRssTask, Task saveRssTask, Task readRssTask) {
        this.view = view;
        this.loadRssTask = loadRssTask;
        this.saveRssTask = saveRssTask;
        this.readRssTask = readRssTask;
    }

    @Override
    public void onAttach() {
        view.hideRssList();
        view.showProgress();

        readRssTask.start(null, readRssResult);
    }

    @Override
    public void onDetach() {
        loadRssTask.stop();

        saveRssTask.stop();

        readRssTask.stop();
    }

    @Override
    public void addRss(String url) {
        view.showProgress();

        loadRssTask.start(url, loadRssResult);
    }

    @Override
    public void deleteRss(RssFeed rss) {
        rssList.remove(rss);

        saveRssTask.start(rssList, saveRssResult);
    }

    private Handler loadRssResult = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            rssList.addAll(0, (ArrayList<RssFeed>) bundle.getSerializable(Task.DATA_KEY));

            saveRssTask.start(rssList, saveRssResult);
        }
    };

    private Handler saveRssResult = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();

            if (bundle.getLong(Task.DATA_KEY) != SAVE_TASK_ERROR_CODE) {
                view.hideProgress();
                view.showRssList();
                view.updateRssList(rssList);
            }
        }
    };

    private Handler readRssResult = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            rssList.addAll(0, (ArrayList<RssFeed>) bundle.getSerializable(Task.DATA_KEY));

            view.hideProgress();

            if (!rssList.isEmpty()) {
                view.showRssList();
                view.updateRssList(rssList);
            }
        }
    };
}
