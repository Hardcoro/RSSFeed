package android.rss.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.rss.model.RssFeedModel;
import android.rss.model.Task;
import android.rss.view.RssFeedView;

public class RssFeedPresenterImpl implements RssFeedPresenter {

    private RssFeedView view;

    private Task loadRssTask;

    public RssFeedPresenterImpl(RssFeedView view, Task loadRssTask) {
        this.view = view;
        this.loadRssTask = loadRssTask;
    }

    @Override
    public void startLoadRss() {
        loadRssTask.start(resultHandler);
    }

    @Override
    public void stopLoadRss() {
        loadRssTask.stop();
    }

    // Handler создается в Главном потоке
    private Handler resultHandler = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(Message message) {
            // Получаем пакет из Сообщения
            Bundle bundle = message.getData();

            // Из пакета получаем Rss каналы
            RssFeedModel rssFeedModel = (RssFeedModel) bundle.getSerializable(Task.DATA_KEY);

            // Показываем базовые данные канала
            view.showRss(rssFeedModel.toString());

            // Показываем каналы
            for (int i = 0; i < rssFeedModel.getItemsSize(); i++) {
                view.showRss(rssFeedModel.getItem(i).toString());
            }
        }
    };
}
