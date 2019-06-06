package android.rss.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.rss.model.RssFeedModel;
import android.rss.model.task.Task;
import android.rss.view.RssFeedView;

public class RssFeedPresenterImpl implements RssFeedPresenter {

    private RssFeedView view;

    private Task loadRssTask;
    private Task saveRssTask;
    private Task readRssTask;

    public RssFeedPresenterImpl(RssFeedView view, Task loadRssTask, Task saveRssTask, Task readRssTask) {
        this.view = view;
        this.loadRssTask = loadRssTask;
        this.saveRssTask = saveRssTask;
        this.readRssTask = readRssTask;
    }

    @Override
    public void startLoadRss() {
        loadRssTask.start(null, resultHandler);
    }

    @Override
    public void stopLoadRss() {
        loadRssTask.stop();

        saveRssTask.stop();

        readRssTask.stop();
    }

    // Handler создается в Главном потоке
    private Handler resultHandler = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(Message message) {
            // Получаем пакет из Сообщения
            Bundle bundle = message.getData();

            // Из пакета получаем Rss каналы
            RssFeedModel rssFeedModel = (RssFeedModel) bundle.getSerializable(Task.DATA_KEY);

            saveRssTask.start(rssFeedModel, null);

            readRssTask.start(null, null);

            // Показываем базовые данные канала
            view.showRss(rssFeedModel.getTitle());

            // Показываем каналы
            for (int i = 0; i < rssFeedModel.getItemsSize(); i++) {
                view.showRss(rssFeedModel.getItem(i).getTitle());
            }
        }
    };
}
